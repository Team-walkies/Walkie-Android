package com.startup.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.main.HomeScreen
import com.startup.home.main.HomeViewModel
import com.startup.home.mypage.MyPageScreen
import com.startup.home.mypage.MyPageViewModel
import com.startup.home.navigation.BottomNavItem
import com.startup.home.navigation.HomeScreenNav
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageScreenNav
import com.startup.home.navigation.WalkieBottomNavigation

@Composable
fun MainBottomNavigationScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    onNavigationEvent: (MainScreenNavigationEvent) -> Unit,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
) {
    val navHostController = rememberNavController()

    LaunchedEffect(Unit) {
        myPageViewModel.event.collect {
            when (it) {
                MainScreenNavigationEvent.MoveToLoginActivity -> {
                    onNavigationEvent.invoke(MainScreenNavigationEvent.MoveToLoginActivity)
                }

                else -> {}
            }
        }
    }
    fun handleHomeScreenNavigationEvent(navigationEvent: HomeScreenNavigationEvent) {
        when (navigationEvent) {
            HomeScreenNavigationEvent.MoveToGainEgg -> {
                navController.navigate(MainScreenNav.HomeGraph.route + "/${HomeScreenNav.GainEgg.route}")
            }

            HomeScreenNavigationEvent.MoveToNotification -> {
                navController.navigate(MainScreenNav.HomeGraph.route + "/${HomeScreenNav.Notification.route}")
            }

            HomeScreenNavigationEvent.MoveToGainCharacter -> {
                navController.navigate(MainScreenNav.HomeGraph.route + "/${HomeScreenNav.GainCharacter.route}")
            }

            HomeScreenNavigationEvent.MoveToSpotArchive -> {
                navController.navigate(MainScreenNav.HomeGraph.route + "/${HomeScreenNav.SpotArchive.route}")
            }
        }
    }

    fun handleMyPageScreenNavigationEvent(navigationEvent: MyPageScreenNavigationEvent) {
        when (navigationEvent) {
            MyPageScreenNavigationEvent.MoveToMyInfo -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.MyInfo.route}")
            }

            MyPageScreenNavigationEvent.MoveToPushSetting -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.PushSetting.route}")
            }

            MyPageScreenNavigationEvent.MoveToNotice -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.Notice.route}")
            }

            MyPageScreenNavigationEvent.MoveToPersonalInfoPolicy -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.PersonalInfoPolicy.route}")
            }

            MyPageScreenNavigationEvent.MoveToRequestUserOpinion -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.RequestUserOpinion.route}")
            }

            is MyPageScreenNavigationEvent.MoveToUnlink -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.Unlink.route}?argument=${navigationEvent.nickName}")
            }

            MyPageScreenNavigationEvent.MoveToLoginActivityWithLogout -> {
                myPageViewModel.logout()
            }

            MyPageScreenNavigationEvent.MoveToNotification -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.Notification.route}")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
        ) {
            NavHost(
                navController = navHostController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        state = homeViewModel.state,
                        onNavigationEvent = ::handleHomeScreenNavigationEvent
                    )
                }
                composable(BottomNavItem.MyPage.route) {
                    MyPageScreen(
                        myInfoViewState = myPageViewModel.state,
                        onNavigationEvent = ::handleMyPageScreenNavigationEvent
                    )
                }
            }
        }

        WalkieBottomNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navHostController,
            onCenterItemClick = {
                onNavigationEvent.invoke(MainScreenNavigationEvent.MoveToSpotActivity)
            }
        )
    }
}
