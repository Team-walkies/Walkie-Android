package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.spot.SpotArchiveScreen
import com.startup.home.spot.SpotReviewModifyScreen

@Composable
fun SpotArchiveNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SpotArchiveScreenNav.SpotArchive.route
    ) {
        composable(SpotArchiveScreenNav.SpotArchive.route) { SpotArchiveScreen() }
        composable(SpotArchiveScreenNav.SpotReviewModify.route) { SpotReviewModifyScreen() }
    }
}