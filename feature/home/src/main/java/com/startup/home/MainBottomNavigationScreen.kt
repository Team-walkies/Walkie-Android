package com.startup.home

import android.app.Activity
import android.app.Activity.RESULT_OK
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.common.event.EventContainer
import com.startup.design_system.widget.toast.ShowToast
import com.startup.home.main.HomeViewModel
import com.startup.home.main.screen.HomeScreen
import com.startup.home.mypage.MyPageViewModel
import com.startup.home.mypage.screen.MyPageScreen
import com.startup.home.navigation.BottomNavItem
import com.startup.home.navigation.HomeScreenNav
import com.startup.home.navigation.MainScreenNav
import com.startup.home.navigation.MyPageScreenNav
import com.startup.home.navigation.WalkieBottomNavigation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

@Composable
fun MainBottomNavigationScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    onNavigationEvent: (MainScreenNavigationEvent) -> Unit,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
) {
    val navHostController = rememberNavController()

    val context = LocalContext.current
    var backPressedOnce by remember { mutableStateOf(false) }
    var showErrorToast by remember { mutableStateOf(false) }
    var errorMessageResId by remember { mutableIntStateOf(R.string.toast_common_error) }
    val canNavigateBack = navHostController.previousBackStackEntry != null
    val lifecycleOwner = LocalLifecycleOwner.current
    val spotMoveActivityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                homeViewModel.notifyViewModelEvent(HomeScreenViewModelEvent.RefreshHome)
            }
        }
    val isHomeRefresh by navController.currentBackStackEntryFlow.map { !canNavigateBack && it.destination.route == MainScreenNav.BottomNavigation.route }
        .collectAsStateWithLifecycle(false)
    BackHandler(enabled = !canNavigateBack) {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
        }
    }
    LaunchedEffect(isHomeRefresh) {
        if (isHomeRefresh) {
            homeViewModel.notifyViewModelEvent(HomeScreenViewModelEvent.RefreshHome)
        }
    }
    // 2초 후 플래그 리셋
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            EventContainer.homeRefreshEventFlow.collect {
                homeViewModel.notifyViewModelEvent(HomeScreenViewModelEvent.RefreshHome)
            }
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            myPageViewModel.event.collect { event ->
                when (event) {
                    MainScreenNavigationEvent.MoveToLoginActivity -> {
                        onNavigationEvent.invoke(MainScreenNavigationEvent.MoveToLoginActivity)
                    }

                    is ErrorToastEvent.ShowToast -> {
                        errorMessageResId = event.messageResId
                        showErrorToast = true
                    }

                    else -> {}
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeViewModel.navigateToGainEgg.collect {
                navController.navigate(MainScreenNav.HomeGraph.route + "/${HomeScreenNav.GainEgg.route}")
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

            MyPageScreenNavigationEvent.MoveToServiceTerm -> {
                navController.navigate(MainScreenNav.MyPageGraph.route + "/${MyPageScreenNav.ServiceTerm.route}")
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
                onNavigationEvent.invoke(
                    MainScreenNavigationEvent.MoveToSpotActivity(
                        spotMoveActivityLauncher
                    )
                )
            }
        )
    }

    if (backPressedOnce) {
        ShowToast(stringResource(R.string.toast_home_back_press), 2_000) {}
    }

    if (showErrorToast) {
        ShowToast(stringResource(errorMessageResId), 2_000) {
            showErrorToast = false
        }
    }
}
