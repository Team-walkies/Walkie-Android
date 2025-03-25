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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.EggHatchingAnimation
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.BatteryOptimizationHelper
import com.startup.common.util.OsVersions
import com.startup.common.util.UsePermissionHelper
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.home.navigation.HomeNavigationGraph
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageNavigationGraph
import com.startup.home.permission.PermissionBottomSheet
import com.startup.home.permission.PermissionState
import com.startup.navigation.LoginModuleNavigator
import com.startup.navigation.SpotModuleNavigator
import com.startup.stepcounter.broadcastReciver.DailyResetReceiver
import com.startup.ui.WalkieTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity<UiEvent, NavigationEvent>(),
    DailyResetReceiver.OnDateChangedListener {
    override val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    // 권한 바텀시트 표시 여부 상태
    private var shouldShowPermissionBottomSheet by mutableStateOf(false)

    // 권한 상태 리스트
    private var permissionStates by mutableStateOf<List<PermissionState>>(emptyList())
    private val dailyResetReceiver = DailyResetReceiver()


    // KSP 는 필드 주입이 안 됨
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
        // UI 이벤트 처리 로직 구현 ex.. 권한 허용 플로우 등등
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        dailyResetReceiver.setOnDateChangedListener(this)
        registerDailyResetReceiver()
        viewModel.initTodayStep()

        setContent {
            WalkieTheme {
                MainScreenWithPermissionBottomSheet()
            }
        }
    }

    // DailyResetReceiver 등록
    private fun registerDailyResetReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_DATE_CHANGED)
        }
        registerReceiver(dailyResetReceiver, filter)
    }

    override fun onDateChanged() {
        viewModel.resetStepCount()
        // todo 기획에 따라 필요하다면
        viewModel.initTodayStep()
    }

    private fun checkPermission() {
        initPermissionStates()
        val allPermissionsGranted = areAllPermissionsGranted()

        if (allPermissionsGranted) {
            startStepCounterService()
            shouldShowPermissionBottomSheet = false
        } else {
            shouldShowPermissionBottomSheet = true
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreenWithPermissionBottomSheet() {
        val sheetState = rememberModalBottomSheetState()
        val coroutineScope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()) {
            MainScreen()

            if (shouldShowPermissionBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        shouldShowPermissionBottomSheet = false
                        if (!areAllPermissionsGranted()) {
                            // 권한 미허용시 앱종료 추후 기획에 따라 조건 변동
                            finish()
                        }
                    },
                    sheetState = sheetState,
                    tonalElevation = 24.dp,
                    dragHandle = {
                        WalkieDragHandle()
                    },
                    containerColor = WalkieTheme.colors.white,
                    contentColor = WalkieTheme.colors.white,
                    scrimColor = WalkieTheme.colors.blackOpacity60,
                ) {
                    PermissionBottomSheet(
                        permissions = permissionStates,
                        onAllPermissionsGranted = {
                            startStepCounterService()
                            coroutineScope.launch {
                                sheetState.hide()
                                shouldShowPermissionBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }


    private fun initPermissionStates() {
        val states = mutableListOf<PermissionState>()

        if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
            states.add(
                PermissionState(
                    type = UsePermissionHelper.Permission.POST_NOTIFICATIONS,
                    isGranted = isGranted(UsePermissionHelper.Permission.POST_NOTIFICATIONS),
                    title = "알림 권한",
                    description = "알림을 받기 위해 필요합니다."
                )
            )
        }

        if (OsVersions.isGreaterThanOrEqualsQ()) {
            states.add(
                PermissionState(
                    type = UsePermissionHelper.Permission.ACTIVITY_RECOGNITION,
                    isGranted = isGranted(UsePermissionHelper.Permission.ACTIVITY_RECOGNITION),
                    title = "신체 활동 권한",
                    description = "걸음 수 측정을 위해 필요합니다."
                )
            )
        }

        states.add(
            PermissionState(
                type = UsePermissionHelper.Permission.BATTERY_OPTIMIZATION,
                isGranted = BatteryOptimizationHelper.isBatteryOptimizationIgnored(this),
                title = "배터리 최적화 제외",
                description = "앱이 백그라운드에서도 정확하게 동작하기 위해 필요합니다."
            )
        )

        permissionStates = states
    }

    private fun areAllPermissionsGranted(): Boolean {
        return permissionStates.all { it.isGranted }
    }

    private fun isGranted(permission: UsePermissionHelper.Permission): Boolean {
        val permissions = UsePermissionHelper.getTypeOfPermission(permission)
        return UsePermissionHelper.isGrantedPermissions(this, *permissions)
    }

    private fun startStepCounterService() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            viewModel.startCounting()
        } else {
            viewModel.stopCounting()
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

            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WalkieTheme.colors.white)
                    .windowInsetsPadding( // 내비게이션 바 인셋을 처리
                        WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
                    ),
            ) {
                NavHost(
                    navController = navController,
                    startDestination = MainScreenNav.BottomNavigation.route
                ) {
                    composable(MainScreenNav.BottomNavigation.route) {
                        MainBottomNavigationScreen(
                            navController,
                            onNavigationEvent = {
                                when (it) {
                                    MainScreenNavigationEvent.MoveToLoginActivity -> {
                                        loginModuleNavigator.navigateLoginView(context = this@HomeActivity)
                                    }

                                    MainScreenNavigationEvent.MoveToSpotActivity -> {
                                        spotModuleNavigator.navigateSpotView(context = this@HomeActivity)
                                    }
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
                            navController
                        )
                    }
                    composable(
                        MainScreenNav.MyPageGraph.route + "/{destination}",
                        arguments = listOf(
                            navArgument("destination") { type = NavType.StringType },
                        )
                    ) { navBackStackEntry ->
                        MyPageNavigationGraph(
                            destinationRoute = navBackStackEntry.arguments?.getString("destination")
                                .orEmpty()
                        )
                    }
                }
            }

            hatchingInfo?.let { trigger ->
                //todo viewmodel or datastore등을 통해서 해당 UI에 캐릭터 정보 / 알등을 전달
                if (trigger) {
                    EggHatchingAnimation(
                        character = "해파리",
                        onDismiss = { viewModel.onHatchingAnimationDismissed() }
                    )
                }
            }
        }
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