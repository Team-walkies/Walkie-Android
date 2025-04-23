package com.startup.home

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.animation.EggHatchingAnimation
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper
import com.startup.design_system.widget.modal.PrimaryTwoButtonModal
import com.startup.home.character.model.CharacterFactory
import com.startup.home.main.HomeViewModel
import com.startup.home.navigation.HomeNavigationGraph
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageNavigationGraph
import com.startup.home.permission.EssentialPermissionBottomSheet
import com.startup.home.permission.NotificationPermissionBottomSheet
import com.startup.home.permission.PermissionBottomSheet
import com.startup.home.permission.PermissionManager
import com.startup.home.permission.PermissionUiEvent
import com.startup.navigation.LoginModuleNavigator
import com.startup.navigation.SpotModuleNavigator
import com.startup.stepcounter.broadcastReciver.DailyResetReceiver
import com.startup.design_system.ui.WalkieTheme
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

    // KSP는 필드 주입이 안 됨
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

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {
    }

    override fun handleUiEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is PermissionUiEvent.ShowActivityRecognitionAlert -> {
                viewModel.setActivityPermissionAlertState(uiEvent.show)
            }

            is PermissionUiEvent.ShowBackgroundLocationAlert -> {
                viewModel.setBackgroundLocationPermssionAlertState(uiEvent.show)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(
            activity = this,
            viewModel = viewModel
        )
        permissionManager.checkPermissions()

        dailyResetReceiver.setOnDateChangedListener(this)
        registerDailyResetReceiver()

        setContent {
            WalkieTheme {
                MainScreenWithPermissionBottomSheet()
            }
        }
    }

    private fun registerDailyResetReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
        registerReceiver(dailyResetReceiver, filter)
    }

    override fun onDateChanged() {
        viewModel.resetStepCount()
    }

    fun startStepCounterService() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            viewModel.startCounting()
        } else {
            viewModel.stopCounting()
        }
    }

    @Composable
    fun MainScreenWithPermissionBottomSheet() {

        LaunchedEffect(Unit) {
            viewModel.uiEventFlow.collect { event ->
                handleUiEvent(event)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            MainScreen()
            HandlePermissionComponents()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HandlePermissionComponents() {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        val permissionUiState = permissionManager.permissionUiState
        val permissionStates = permissionManager.permissionStates

        if (permissionUiState.showPermissionSettingsDialog) {
            val activityPermissionGranted = UsePermissionHelper.isGrantedPermissions(
                this@HomeActivity,
                *UsePermissionHelper.getTypeOfPermission(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION)
            )

            val locationPermissionGranted = UsePermissionHelper.isGrantedPermissions(
                this@HomeActivity,
                *UsePermissionHelper.getTypeOfPermission(UsePermissionHelper.Permission.FOREGROUND_LOCATION)
            )

            val bothPermissionsDenied = !activityPermissionGranted && !locationPermissionGranted
            val onlyActivityDenied = !activityPermissionGranted && locationPermissionGranted
            val onlyLocationDenied = activityPermissionGranted && !locationPermissionGranted

            val titleResId = when {
                bothPermissionsDenied -> R.string.permission_essential_dialog_title
                onlyActivityDenied -> R.string.permission_activity_recognition_title
                onlyLocationDenied -> R.string.permission_location_dialog_title
                else -> R.string.permission_essential_dialog_title
            }

            val messageResId = when {
                bothPermissionsDenied -> R.string.permission_essential_dialog_message
                onlyActivityDenied -> R.string.permission_activity_recognition_message
                onlyLocationDenied -> R.string.permission_location_dialog_message
                else -> R.string.permission_essential_dialog_message
            }

            val textAlign = when {
                bothPermissionsDenied || onlyActivityDenied -> TextAlign.Center
                onlyLocationDenied -> TextAlign.Start
                else -> TextAlign.Center
            }

            PrimaryTwoButtonModal(
                title = stringResource(titleResId),
                subTitle = stringResource(messageResId),
                negativeText = stringResource(R.string.permission_dialog_negative),
                positiveText = stringResource(R.string.permission_dialog_positive),
                onClickNegative = {
                    permissionManager.closePermissionSettingsDialog()
                },
                onClickPositive = {
                    permissionManager.closePermissionSettingsDialog(goToSettings = true)
                },
                textAlign = textAlign
            )
        }

        // 필수 권한 바텀 시트
        if (permissionUiState.showEssentialPermissionSheet) {
            PermissionBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    permissionManager.handleEssentialPermissionDismiss()
                }
            ) {
                EssentialPermissionBottomSheet(
                    permissions = permissionStates,
                    onAllPermissionsGranted = {
                        permissionManager.onEssentialPermissionsGranted()
                    },
                    onShowRationale = {
                        permissionManager.handlePermissionRationale()
                    },
                    onNeverAskAgain = {
                        permissionManager.handleNeverAskAgainPermissions()
                    }
                )
            }
        }

        // 알림 권한 바텀 시트
        if (permissionUiState.showNotificationPermissionSheet) {
            PermissionBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    permissionManager.handleNotificationPermissionEvents(
                        PermissionManager.NotificationAction.DISMISS
                    )
                }
            ) {
                NotificationPermissionBottomSheet(
                    onDismiss = {
                        permissionManager.handleNotificationPermissionEvents(
                            PermissionManager.NotificationAction.DISMISS
                        )
                    },
                    onAllowPermission = {
                        permissionManager.handleNotificationPermissionEvents(
                            PermissionManager.NotificationAction.ALLOW
                        )
                    },
                    onShowRationale = {
                        permissionManager.handleNotificationPermissionEvents(
                            PermissionManager.NotificationAction.SHOW_RATIONALE
                        )
                    },
                    onNeverAskAgain = {
                        permissionManager.handleNotificationPermissionEvents(
                            PermissionManager.NotificationAction.NEVER_ASK_AGAIN
                        )
                    }
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

        val hatchingInfo by viewModel.hatchingInfo.collectAsStateWithLifecycle()

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

                                    MainScreenNavigationEvent.MoveToSpotActivity -> {
                                        spotModuleNavigator.navigateSpotView(context = this@HomeActivity)
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

            hatchingInfo.let {
                val hatchingInfoState by viewModel.hatchingInfo.collectAsStateWithLifecycle()

                if (hatchingInfoState.data.isHatching) {
                    val character = hatchingInfoState.data.character
                    val eggKind = hatchingInfoState.data.eggKind

                    val characterName = stringResource(id = character.characterNameResId)
                    val allCharacters = CharacterFactory.getAllCharacters()
                    val foundCharacter = allCharacters.find {
                        it.imageResource == character.characterImageResId
                    }
                    val characterRank = foundCharacter?.rarity?.ordinal?.minus(1) ?: 0
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
    }

    override fun onResume() {
        super.onResume()
        permissionManager.checkOnResume()
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
    interface LoginNavigatorEntryPoint {
        fun loginNavigatorNavigator(): LoginModuleNavigator
    }

    override fun onDestroy() {
        unregisterReceiver(dailyResetReceiver)
        dailyResetReceiver.removeOnDateChangedListener()
        super.onDestroy()
    }
}