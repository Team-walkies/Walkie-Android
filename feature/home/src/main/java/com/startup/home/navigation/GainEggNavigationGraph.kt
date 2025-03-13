package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.TempViewModel
import com.startup.home.egg.EggGainProbabilityScreen
import com.startup.home.egg.GainEggScreen

@Composable
fun GainEggNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = GainEggScreenNav.GainEggNav.route
    ) {
        composable(GainEggScreenNav.GainEggNav.route) { GainEggScreen(emptyList()) }
        composable(GainEggScreenNav.EggGainProbabilityNav.route) { EggGainProbabilityScreen() }
    }
}