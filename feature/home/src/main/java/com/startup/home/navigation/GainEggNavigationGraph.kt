package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.common.util.DateUtil
import com.startup.home.egg.EggGainProbabilityScreen
import com.startup.home.egg.GainEggScreen
import com.startup.home.egg.GainEggScreenNavigationEvent
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.MyEggModel

@Composable
fun GainEggNavigationGraph(parentNavController: NavHostController) {
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
                listOf(
                    MyEggModel(
                        eggId = 0,
                        nowStep = 8334,
                        needStep = 10000,
                        play = true,
                        characterId = -1,
                        eggKind = EggKind.Legend,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 3,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Legend,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 2,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Epic,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 4,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Legend,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 5,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Epic,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 6,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Legend,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    ), MyEggModel(
                        eggId = 7,
                        nowStep = 8334,
                        needStep = 10000,
                        play = false,
                        characterId = -1,
                        eggKind = EggKind.Epic,
                        obtainedPosition = "대전시 유성구",
                        obtainedDate = DateUtil.convertDateFormat("2024-01-12 12:20:10"),
                    )
                ),
                ::handleGainEggScreenNavigationEvent
            )
        }
        composable(GainEggScreenNav.EggGainProbabilityNav.route) { EggGainProbabilityScreen { navController.navigateUp() } }
    }
}