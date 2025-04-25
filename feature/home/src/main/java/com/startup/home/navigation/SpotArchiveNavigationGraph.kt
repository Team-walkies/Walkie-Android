package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.MainScreenNavigationEvent
import com.startup.home.spot.SpotArchiveScreen
import com.startup.home.spot.SpotArchiveViewModel
import com.startup.home.spot.SpotReviewModifyScreen
import com.startup.home.spot.model.SpotArchiveUiEvent

@Composable
fun SpotArchiveNavigationGraph(parentNavController: NavHostController, spotArchiveViewModel: SpotArchiveViewModel = hiltViewModel(), onNavigationEvent: (MainScreenNavigationEvent) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SpotArchiveScreenNav.SpotArchive.route
    ) {
        composable(SpotArchiveScreenNav.SpotArchive.route) {
            SpotArchiveScreen(
                spotArchiveViewModel.state
            ) {
                when (it) {
                    SpotArchiveUiEvent.OnBack -> {
                        parentNavController.navigateUp()
                    }
                    is SpotArchiveUiEvent.OnDateChanged -> {
                        spotArchiveViewModel.changedSelectedDate(it.calendarModel)
                    }

                    is SpotArchiveUiEvent.OnModifyReview -> {
                        onNavigationEvent.invoke(MainScreenNavigationEvent.MoveToSpotModifyActivity(it.launcher, it.intent))
                    }

                    is SpotArchiveUiEvent.OnDeleteReview -> {
                        spotArchiveViewModel.deleteReview(it.review)
                    }
                    is SpotArchiveUiEvent.RefreshReviewList -> {
                        spotArchiveViewModel.initializeReviewList()
                    }
                }
            }
        }
        composable(SpotArchiveScreenNav.SpotReviewModify.route) { SpotReviewModifyScreen() }
    }
}