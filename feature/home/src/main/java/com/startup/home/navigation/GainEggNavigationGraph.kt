package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.egg.screen.EggGainProbabilityScreen
import com.startup.home.egg.GainEggScreenNavigationEvent
import com.startup.home.egg.GainEggViewModel
import com.startup.home.egg.GainEggUiEvent
import com.startup.home.egg.screen.GainEggScreen

@Composable
fun GainEggNavigationGraph(
    parentNavController: NavHostController,
    gainEggViewModel: GainEggViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    fun handleGainEggScreenNavigationEvent(navigationEvent: GainEggScreenNavigationEvent) {
        when (navigationEvent) {
            GainEggScreenNavigationEvent.MoveToEggGainProbabilityScreen -> {
                navController.navigate(GainEggScreenNav.EggGainProbabilityNav.route)
            }

            GainEggScreenNavigationEvent.Back -> {
                parentNavController.navigateUp()
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = GainEggScreenNav.GainEggNav.route
    ) {
        composable(GainEggScreenNav.GainEggNav.route) {
            GainEggScreen(
                gainEggViewModel.state,
                ::handleGainEggScreenNavigationEvent
            ) { uiEvent ->
                when (uiEvent) {
                    is GainEggUiEvent.OnChangedClickWalkEgg -> {
                        gainEggViewModel.updateEgg(
                            eggId = uiEvent.eggId,
                            needStep = uiEvent.needStep,
                            nowStep = uiEvent.nowStep
                        )
                    }
                }
            }
        }
        composable(GainEggScreenNav.EggGainProbabilityNav.route) { EggGainProbabilityScreen { navController.navigateUp() } }
    }
}