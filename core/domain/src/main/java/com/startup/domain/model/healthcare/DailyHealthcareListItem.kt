package com.startup.domain.model.healthcare

data class DailyHealthcareListItem(
    val nowSteps: Int,
    val date: String,
    val targetSteps: Int
)