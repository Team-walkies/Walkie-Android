package com.startup.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.startup.home.spot.SpotArchiveScreen
import com.startup.home.spot.SpotReviewModifyScreen
import com.startup.home.spot.model.ReviewModel
import java.time.LocalDate

@Composable
fun SpotArchiveNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SpotArchiveScreenNav.SpotArchive.route
    ) {
        composable(SpotArchiveScreenNav.SpotArchive.route) {
            SpotArchiveScreen(
                list = listOf(
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = false,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    ),
                    ReviewModel(
                        reviewId = 1,
                        reviewCd = true,
                        review = "하이",
                        date = LocalDate.now(),
                        pic = "",
                        timeRange = "오후 01:45 ~ 04:10",
                        rating = 2,
                        moveDuration = "10h 20m",
                        characterId = 1,
                        step = "2,000",
                        spotId = 1,
                        distance = 23.toDouble()
                    )
                )
            )
        }
        composable(SpotArchiveScreenNav.SpotReviewModify.route) { SpotReviewModifyScreen() }
    }
}