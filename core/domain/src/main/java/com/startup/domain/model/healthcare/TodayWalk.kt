package com.startup.domain.model.healthcare

data class TodayWalk(
    val nowCalories: Int,
    val nowDistance: Int,
    val nowSteps: Int,
    val targetSteps: Int
)