package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.base.NavigationEvent
import com.startup.domain.model.notice.Notice
import com.startup.home.mypage.NoticeDetailScreen
import com.startup.home.mypage.NoticeScreen
import com.startup.home.mypage.NoticeViewModel
import com.startup.home.mypage.model.NoticeScreenNavigationEvent

@Composable
fun NoticeNavigationGraph(
    parentNavController: NavHostController,
    noticeViewModel: NoticeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    fun backPress() {
        val isBackStackExist = navController.navigateUp()
        if (!isBackStackExist) {
            parentNavController.navigateUp()
        }
    }

    fun handleNavigationEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent) {
            NavigationEvent.Back -> {
                backPress()
            }
        }
    }

    fun handleNoticeScreenNavigationEvent(navigationEvent: NoticeScreenNavigationEvent) {
        when (navigationEvent) {
            is NoticeScreenNavigationEvent.MoveToNoticeDetail -> {
                val notice = navigationEvent.notice
                navController.navigate(NoticeScreenNav.NoticeDetail.route + "/${notice.date}/${notice.title}/${notice.detail}")
            }

            NoticeScreenNavigationEvent.Back -> {
                backPress()
            }
        }
    }

    LaunchedEffect(Unit) {
        noticeViewModel.fetchNoticeList()
    }
    NavHost(
        navController = navController,
        startDestination = NoticeScreenNav.NoticeList.route
    ) {
        composable(
            route = NoticeScreenNav.NoticeDetail.route + "/{notice_date}/{notice_title}/{notice_detail}",
            arguments = listOf(
                navArgument("notice_date") { type = NavType.StringType },
                navArgument("notice_title") { type = NavType.StringType },
                navArgument("notice_detail") { type = NavType.StringType },
            )
        ) { navBackStackEntry ->
            val noticeDate = navBackStackEntry.arguments?.getString("notice_date").orEmpty()
            val noticeTitle = navBackStackEntry.arguments?.getString("notice_title").orEmpty()
            val noticeDetail = navBackStackEntry.arguments?.getString("notice_detail").orEmpty()
            val notice = Notice(noticeDate, noticeTitle, noticeDetail)
            NoticeDetailScreen(notice, onNavigationEvent = ::handleNavigationEvent)
        }
        composable(NoticeScreenNav.NoticeList.route) {
            NoticeScreen(
                noticeViewModel.state,
                onNavigationEvent = ::handleNoticeScreenNavigationEvent
            )
        }
    }
}