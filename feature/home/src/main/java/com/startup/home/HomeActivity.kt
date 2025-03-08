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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.OsVersions
import com.startup.home.mypage.MyPageScreen
import com.startup.navigation.BottomNavItem
import com.startup.navigation.WalkieBottomNavigation
import com.startup.ui.WalkieTheme
import dagger.hilt.android.AndroidEntryPoint
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
        val navController = rememberNavController()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                WalkieBottomNavigation(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen()
                }
                composable(BottomNavItem.Spot.route) {
                    //todo feature spot 모듈에서 접근
                }
                composable(BottomNavItem.MyPage.route) {
                    MyPageScreen()
                }
            }
        }
    }

}