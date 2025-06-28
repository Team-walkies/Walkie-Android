package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName
import com.startup.domain.model.egg.DailyEgg

data class DailyEggResponse(
    @SerializedName("receivedToday")
    val receivedToday: Boolean?,
    @SerializedName("remainingDays")
    val remainingDays: Int?
) {
    fun toDomain() = DailyEgg(
        receivedToday = receivedToday ?: false,
        remainingDays = remainingDays ?: 0
    )
}