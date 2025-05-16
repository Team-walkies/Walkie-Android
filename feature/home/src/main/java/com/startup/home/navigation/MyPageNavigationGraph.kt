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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.base.NavigationEvent
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.toast.ShowToast
import com.startup.home.ErrorToastEvent
import com.startup.home.MainScreenNavigationEvent
import com.startup.home.R
import com.startup.home.mypage.MyInfoUIEvent
import com.startup.home.mypage.MyPageViewModel
import com.startup.home.mypage.PushSettingUIEvent
import com.startup.home.mypage.UnlinkUiEvent
import com.startup.home.mypage.screen.MyInfoScreen
import com.startup.home.mypage.screen.PersonalInfoPolicyScreen
import com.startup.home.mypage.screen.PushSettingScreen
import com.startup.home.mypage.screen.RequestUserOpinionScreen
import com.startup.home.mypage.screen.ServiceTermScreen
import com.startup.home.mypage.screen.UnlinkScreen
import com.startup.home.notification.screen.NotificationListScreen

@Composable
fun MyPageNavigationGraph(
    destinationRoute: String,
    parentNavController: NavHostController,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    onNavigationEvent: (MainScreenNavigationEvent) -> Unit,
) {
    val navController = rememberNavController()
    val snackBarHostState = SnackbarHostState()
    // 홈화면(각각의 NavGraph), 지도화면(각각의 NavGraph), 마이페이지 화면(각각의 NavGraph)
    var showErrorToast by remember { mutableStateOf(false) }
    var errorMessageResId by remember { mutableIntStateOf(R.string.toast_common_error) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            myPageViewModel.event.collect {
                when (it) {
                    MainScreenNavigationEvent.MoveToLoginActivity -> {
                        onNavigationEvent.invoke(MainScreenNavigationEvent.MoveToLoginActivity)
                    }

                    is ErrorToastEvent.ShowToast -> {
                        errorMessageResId = it.messageResId
                        showErrorToast = true
                    }

                    else -> {}
                }
            }
        }
    }
    fun backPress() {
        val isBackStackExist = navController.navigateUp()
        if (!isBackStackExist) {
            parentNavController.navigateUp()
        }
    }

    fun handleMyInfoUiEvent(event: MyInfoUIEvent) {
        when (event) {
            is MyInfoUIEvent.OnChangedProfileAccessToggle -> {
                myPageViewModel.updateProfileAccess()
            }
        }
    }

    fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.Back -> {
                backPress()
            }
        }
    }

    fun handlePushSettingUiEvent(event: PushSettingUIEvent) {
        when (event) {
            is PushSettingUIEvent.OnChangedTodayStepNoti -> {
                myPageViewModel.updateTodayStepNoti(event.enabled)
            }

            is PushSettingUIEvent.OnChangedArriveSpotNoti -> {
                myPageViewModel.updateArriveSpotNoti(event.enabled)
            }

            is PushSettingUIEvent.OnChangedEggHatchedNoti -> {
                myPageViewModel.updateEggHatchedNoti(event.enabled)
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
                composable(MyPageScreenNav.MyInfo.route) {
                    MyInfoScreen(
                        viewState = myPageViewModel.state,
                        uiEventSender = ::handleMyInfoUiEvent,
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(MyPageScreenNav.PushSetting.route) {
                    PushSettingScreen(
                        viewState = myPageViewModel.state,
                        uiEventSender = ::handlePushSettingUiEvent,
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(MyPageScreenNav.Notice.route) {
                    NoticeNavigationGraph(parentNavController = parentNavController)
                }
                composable(MyPageScreenNav.PersonalInfoPolicy.route) {
                    PersonalInfoPolicyScreen(
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(MyPageScreenNav.ServiceTerm.route) {
                    ServiceTermScreen(
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(MyPageScreenNav.RequestUserOpinion.route) {
                    RequestUserOpinionScreen(
                        onNavigationEvent = ::handleNavigationEvent
                    )
                }
                composable(
                    route = MyPageScreenNav.Unlink.route + "/{argument}", arguments = listOf(
                        navArgument("argument") { type = NavType.StringType },
                    )
                ) { navBackStackEntry ->
                    val nickName = navBackStackEntry.arguments?.getString("argument").orEmpty()
                    UnlinkScreen(
                        userNickName = nickName,
                        onNavigationEvent = ::handleNavigationEvent,
                        uiEventSender = {
                            when (it) {
                                UnlinkUiEvent.UnlinkWalkie -> {
                                    myPageViewModel.unLink()
                                }
                            }
                        })
                }
                composable(MyPageScreenNav.Notification.route) {
                    NotificationListScreen(onNavigationEvent = {
                        when (it) {
                            NavigationEvent.Back -> {
                                backPress()
                            }
                        }
                    })
                }
            }
        }
    }

    if (showErrorToast) {
        ShowToast(stringResource(errorMessageResId), 2_000) {
            showErrorToast = false
        }
    }
}
