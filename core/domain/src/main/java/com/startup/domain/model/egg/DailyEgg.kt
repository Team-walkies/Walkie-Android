package com.startup.domain.model.egg

data class DailyEgg(
    val receivedToday: Boolean,
    val remainingDays: Int
)