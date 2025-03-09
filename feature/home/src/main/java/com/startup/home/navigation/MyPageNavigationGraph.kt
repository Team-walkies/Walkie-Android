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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.mypage.MyInfoScreen
import com.startup.home.mypage.NoticeScreen
import com.startup.home.mypage.PersonalInfoPolicyScreen
import com.startup.home.mypage.PushSettingScreen
import com.startup.home.mypage.RequestUserOpinionScreen
import com.startup.home.mypage.UnlinkScreen
import com.startup.ui.WalkieTheme

@Composable
fun MyPageNavigationGraph(destinationRoute: String) {
    val navController = rememberNavController()
    val snackBarHostState = SnackbarHostState()
    // 홈화면(각각의 NavGraph), 지도화면(각각의 NavGraph), 마이페이지 화면(각각의 NavGraph)
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
                composable(MyPageScreenNav.MyInfo.route) { MyInfoScreen() }
                composable(MyPageScreenNav.PushSetting.route) { PushSettingScreen() }
                composable(MyPageScreenNav.Notice.route) { NoticeScreen() }
                composable(MyPageScreenNav.PersonalInfoPolicy.route) { PersonalInfoPolicyScreen() }
                composable(MyPageScreenNav.RequestUserOpinion.route) { RequestUserOpinionScreen() }
                composable(MyPageScreenNav.Unlink.route) { UnlinkScreen() }
            }
        }
    }
}