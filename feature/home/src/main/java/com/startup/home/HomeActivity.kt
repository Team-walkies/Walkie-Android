package com.startup.home

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.animation.EggHatchingAnimation
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper
import com.startup.design_system.ui.WalkieTheme
import com.startup.ga.AnalyticsHelper
import com.startup.ga.LocalAnalyticsHelper
import com.startup.home.main.HomeViewModel
import com.startup.home.navigation.HomeNavigationGraph
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageNavigationGraph
import com.startup.home.permission.HandlePermissionComponents
import com.startup.home.permission.PermissionManager
import com.startup.home.permission.PermissionUiEvent
import com.startup.navigation.LoginModuleNavigator
import com.startup.navigation.SpotModuleNavigator
import com.startup.stepcounter.broadcastReciver.DailyResetReceiver
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class HomeActivity : BaseActivity<UiEvent, NavigationEvent>(),
    DailyResetReceiver.OnDateChangedListener {

    override val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    private lateinit var permissionManager: PermissionManager

    // 날짜 변경 수신기
    private val dailyResetReceiver = DailyResetReceiver()

    // Activity Result Launchers - 권한 관련 Intent 처리용
    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // 설정에서 돌아온 후 배터리 최적화 단계로 진행
        permissionManager.proceedToBatteryOptimization()
    }

    private val batteryOptimizationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // 배터리 최적화 설정에서 돌아온 후 알림 권한 단계로 진행
        permissionManager.proceedToNotification()
    }

    // KSP는 필드 주입이 안 되므로 EntryPoint 사용
    private val loginModuleNavigator: LoginModuleNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            LoginNavigatorEntryPoint::class.java
        ).loginNavigatorNavigator()
    }

    private val spotModuleNavigator: SpotModuleNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            SpotNavigatorEntryPoint::class.java
        ).spotNavigatorNavigator()
    }

    private val analyticsHelper: AnalyticsHelper by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            AnalyticsHelperEntryPoint::class.java
        ).analyticsHelper()
    }

    private val permissionCallbacks = object : PermissionManager.PermissionManagerCallbacks {
        override fun startStepCounterService() {
            if (OsVersions.isGreaterThanOrEqualsO()) {
                viewModel.startCounting()
            } else {
                viewModel.stopCounting()
            }
        }

        override fun emitUiEvent(event: UiEvent) {
            viewModel.emitUiEvent(event)
        }

        override fun launchPermissionSettings() {
            val intent = UsePermissionHelper.getPermissionSettingsIntent(this@HomeActivity)
            settingsLauncher.launch(intent)
        }

        override fun launchBatteryOptimizationSettings() {
            val intent =
                BatteryOptimizationHelper.getBatteryOptimizationSettingsIntent(this@HomeActivity)
            batteryOptimizationLauncher.launch(intent)
        }
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {
    }

    override fun handleUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is PermissionUiEvent.ShowActivityRecognitionAlert -> {
                viewModel.setActivityPermissionAlertState(uiEvent.show)
            }

            is PermissionUiEvent.ShowBackgroundLocationAlert -> {
                viewModel.setBackgroundLocationPermissionAlertState(uiEvent.show)
            }

            is PermissionUiEvent.UpdateAllPermissionAlerts -> {
                viewModel.setActivityPermissionAlertState(uiEvent.showActivityAlert)
                viewModel.setBackgroundLocationPermissionAlertState(uiEvent.showLocationAlert)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissionManager()
        registerDailyResetReceiver()
        setContent {
            WalkieTheme {
                MainScreenWithPermissionComponents()
            }
        }
    }

    private fun setupPermissionManager() {
        permissionManager = PermissionManager(
            context = this,
            callbacks = permissionCallbacks
        )
        permissionManager.checkPermissions()
    }

    private fun registerDailyResetReceiver() {
        dailyResetReceiver.setOnDateChangedListener(this)

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
        registerReceiver(dailyResetReceiver, filter)
    }

    override fun onDateChanged() {
        viewModel.resetTodayStepCount()
    }

    @Composable
    fun MainScreenWithPermissionComponents() {
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEventFlow.collect { event ->
                    handleUiEvent(event)
                }
            }
        }

        CompositionLocalProvider(
            LocalAnalyticsHelper provides analyticsHelper,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                MainScreen()
                HandlePermissionComponents(
                    context = this@HomeActivity,
                    permissionManager = permissionManager
                )
            }
        }
    }

    @Composable
    fun MainScreen() {
        MainContent()
    }

    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        val snackBarHostState = SnackbarHostState()

        Scaffold(
            snackbarHost = {
                // 원래 코드 유지
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WalkieTheme.colors.white)
                    .windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
                    ),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = MainScreenNav.BottomNavigation.route
                ) {
                    // 원래 Navigation Graph 코드 유지
                    composable(MainScreenNav.BottomNavigation.route) {
                        MainBottomNavigationScreen(
                            navController,
                            viewModel,
                            onNavigationEvent = {
                                when (it) {
                                    MainScreenNavigationEvent.MoveToLoginActivity -> {
                                        loginModuleNavigator.navigateLoginView(context = this@HomeActivity)
                                        finish()
                                    }

                                    is MainScreenNavigationEvent.MoveToSpotActivity -> {
                                        spotModuleNavigator.navigateSpotView(
                                            activity = this@HomeActivity,
                                            launcher = it.launcher
                                        )
                                    }

                                    else -> {}
                                }
                            },
                        )
                    }
                    composable(
                        route = MainScreenNav.HomeGraph.route + "/{destination}",
                        arguments = listOf(
                            navArgument("destination") { type = NavType.StringType },
                        )
                    ) { navBackStackEntry ->
                        HomeNavigationGraph(
                            destinationRoute = navBackStackEntry.arguments?.getString("destination")
                                .orEmpty(),
                            navController,
                            onNavigationEvent = {
                                when (it) {
                                    is MainScreenNavigationEvent.MoveToSpotModifyActivity -> {
                                        spotModuleNavigator.navigateSpotModifyView(
                                            launcher = it.launcher,
                                            activity = this@HomeActivity,
                                            intentBuilder = it.intent
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        )
                    }
                    composable(
                        MainScreenNav.MyPageGraph.route + "/{destination}?argument={argument}",
                        arguments = listOf(
                            navArgument("destination") { type = NavType.StringType },
                            navArgument("argument") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            },
                        )
                    ) { navBackStackEntry ->
                        val arguments = navBackStackEntry.arguments?.getString("argument")
                        MyPageNavigationGraph(
                            destinationRoute = navBackStackEntry.arguments?.getString("destination")
                                .orEmpty().run {
                                    if (arguments != null) {
                                        "$this/${arguments}"
                                    } else {
                                        this
                                    }
                                },
                            parentNavController = navController,
                            onNavigationEvent = {
                                when (it) {
                                    MainScreenNavigationEvent.MoveToLoginActivity -> {
                                        loginModuleNavigator.navigateLoginView(context = this@HomeActivity)
                                        finish()
                                    }

                                    else -> {}
                                }
                            },
                        )
                    }
                }
            }

            val hatchingInfoState by viewModel.hatchingInfo.collectAsStateWithLifecycle()

            if (hatchingInfoState.data.isHatching) {
                val character = hatchingInfoState.data.character
                val eggKind = hatchingInfoState.data.eggKind

                val characterName = stringResource(id = character.characterNameResId)
                val eggRank = eggKind.ordinal - 1

                EggHatchingAnimation(
                    characterName = characterName,
                    characterImageResId = character.characterImageResId,
                    eggRank = eggRank,
                    onDismiss = { viewModel.onHatchingAnimationDismissed() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        permissionManager.checkOnResume()
    }

    override fun onDestroy() {
        unregisterReceiver(dailyResetReceiver)
        dailyResetReceiver.removeOnDateChangedListener()
        super.onDestroy()
    }

    override fun navigateToLogin() {
        loginModuleNavigator.navigateLoginView(this)
        finish()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SpotNavigatorEntryPoint {
        fun spotNavigatorNavigator(): SpotModuleNavigator
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AnalyticsHelperEntryPoint {
        fun analyticsHelper(): AnalyticsHelper
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LoginNavigatorEntryPoint {
        fun loginNavigatorNavigator(): LoginModuleNavigator
    }
}