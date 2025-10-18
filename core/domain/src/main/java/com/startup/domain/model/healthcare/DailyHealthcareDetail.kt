package com.startup.domain.model.healthcare

data class DailyHealthcareDetail(
    val nowCalories: Int,
    val nowDistance: Double,
    val nowSteps: Int,
    val targetSteps: Int,
    val award: String
)
