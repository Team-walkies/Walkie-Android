package com.startup.home.navigation

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.common.base.NavigationEvent
import com.startup.design_system.ui.WalkieTheme
import com.startup.home.MainScreenNavigationEvent
import com.startup.home.character.HatchingCharacterScreen
import com.startup.home.character.HatchingCharacterViewModel
import com.startup.home.notification.NotificationListScreen

@Composable
fun HomeNavigationGraph(
    destinationRoute: String,
    parentNavController: NavHostController,
    hatchingCharacterViewModel: HatchingCharacterViewModel = hiltViewModel(),
    onNavigationEvent: (MainScreenNavigationEvent) -> Unit
) {
    val navController = rememberNavController()
    val snackBarHostState = SnackbarHostState()
    // 홈화면(각각의 NavGraph), 지도화면(각각의 NavGraph), 마이페이지 화면(각각의 NavGraph)

    fun backPressed() {
        val isBackStackExist = navController.navigateUp()
        if (!isBackStackExist) {
            parentNavController.navigateUp()
        }
    }

    fun handleNavigationEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent) {
            NavigationEvent.Back -> {
                backPressed()
            }
        }
    }
    Scaffold(
        snackbarHost = {
//            SnackBar(snackBarHostState = snackBarHostState)
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
                startDestination = destinationRoute
            ) {
                composable(HomeScreenNav.GainEgg.route) { GainEggNavigationGraph(parentNavController = parentNavController) }
                composable(HomeScreenNav.GainCharacter.route) {
                    HatchingCharacterScreen(
                        hatchingCharacterViewModel.state,
                        onClickPartner = hatchingCharacterViewModel::onClickPartner,
                        onSelectPartner = hatchingCharacterViewModel::onSelectPartner,
                        onDismissBottomSheet = hatchingCharacterViewModel::clearViewingPartner,
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(HomeScreenNav.SpotArchive.route) {
                    SpotArchiveNavigationGraph(
                        parentNavController = parentNavController,
                        onNavigationEvent = onNavigationEvent
                    )
                }
                composable(HomeScreenNav.Notification.route) {
                    NotificationListScreen(onNavigationEvent = ::handleNavigationEvent)
                }
            }
        }
    }
}