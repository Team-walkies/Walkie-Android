package com.startup.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.home.mypage.MyPageScreen
import com.startup.navigation.BottomNavItem
import com.startup.navigation.WalkieBottomNavigation
import com.startup.ui.WalkieTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class HomeActivity : BaseActivity<UiEvent, NavigationEvent>() {
    override val viewModel: TempViewModel by viewModels<TempViewModel>()

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {
    }

    override fun handleUiEvent(uiEvent: UiEvent) {
        // UI 이벤트 처리 로직 구현 ex.. 권한 허용 플로우 등등
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WalkieTheme {
                MainScreen()
            }
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