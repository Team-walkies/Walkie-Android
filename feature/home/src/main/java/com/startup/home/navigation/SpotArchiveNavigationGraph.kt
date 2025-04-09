package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.spot.SpotArchiveScreen
import com.startup.home.spot.SpotArchiveViewModel
import com.startup.home.spot.SpotReviewModifyScreen

@Composable
fun SpotArchiveNavigationGraph(spotArchiveViewModel: SpotArchiveViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SpotArchiveScreenNav.SpotArchive.route
    ) {
        composable(SpotArchiveScreenNav.SpotArchive.route) {
            SpotArchiveScreen(
                spotArchiveViewModel.state,
                spotArchiveViewModel::changedSelectedDate
            )
        }
        composable(SpotArchiveScreenNav.SpotReviewModify.route) { SpotReviewModifyScreen() }
    }
}