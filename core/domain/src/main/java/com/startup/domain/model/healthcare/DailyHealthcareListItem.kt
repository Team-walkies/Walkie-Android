package com.startup.domain.model.healthcare

import java.time.LocalDate

data class DailyHealthcareListItem(
    val nowSteps: Int,
    val date: LocalDate,
    val targetSteps: Int,
    val award: Boolean
)