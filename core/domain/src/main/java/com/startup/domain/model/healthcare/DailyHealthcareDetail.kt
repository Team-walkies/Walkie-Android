package com.startup.domain.model.healthcare

data class DailyHealthcareDetail(
    val caloriesDescription: String,
    val caloriesName: String,
    val caloriesUrl: String,
    val nowCalories: Int,
    val nowDistance: Int,
    val nowSteps: Int,
    val targetSteps: Int
)
