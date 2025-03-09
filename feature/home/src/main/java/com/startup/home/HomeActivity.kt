package com.startup.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
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
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.OsVersions
import com.startup.home.navigation.HomeNavigationGraph
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageNavigationGraph
import com.startup.navigation.HomeFeatureNavigator
import com.startup.ui.WalkieTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class HomeActivity : BaseActivity<UiEvent, NavigationEvent>() {
    override val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    private val requiredPermissions = if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    } else if (OsVersions.isGreaterThanOrEqualsQ()) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    } else {
        arrayOf()
    }

    // KSP 는 필드 주입이 안 됨
    private val homeFeatureNavigator: HomeFeatureNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            HomeFeatureNavigatorEntryPoint::class.java
        ).homeFeatureNavigator()
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {
    }

    override fun handleUiEvent(uiEvent: UiEvent) {
        // UI 이벤트 처리 로직 구현 ex.. 권한 허용 플로우 등등
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkBatteryOptimization()
        checkAndRequestPermissions()

        setContent {
            WalkieTheme {
                MainScreen()
            }
        }
    }

    private fun checkBatteryOptimization() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:$packageName")
                startActivity(this)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (!hasRequiredPermissions()) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    startStepCounterService()
                }
            }.launch(requiredPermissions)
        } else {
            startStepCounterService()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
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
                                        homeFeatureNavigator.navigateLoginView(context = this@HomeActivity)
                                    }

                                    MainScreenNavigationEvent.MoveToSpotActivity -> {
                                        homeFeatureNavigator.navigateSpotView(context = this@HomeActivity)
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
                    composable(MainScreenNav.MyPageGraph.route) { navBackStackEntry ->
                        MyPageNavigationGraph(
                            destinationRoute = navBackStackEntry.arguments?.getString("destination")
                                .orEmpty()
                        )
                    }
                }
            }
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HomeFeatureNavigatorEntryPoint {
        fun homeFeatureNavigator(): HomeFeatureNavigator
    }

}