package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class ResponseDailyHealthcareDetail(
    @SerializedName("nowCalories")
    val nowCalories: Int?,
    @SerializedName("nowDistance")
    val nowDistance: Double?,
    @SerializedName("nowSteps")
    val nowSteps: Int?,
    @SerializedName("targetSteps")
    val targetSteps: Int?,
    @SerializedName("award")
    val award: String?
) {
    fun toDomain(): DailyHealthcareDetail = DailyHealthcareDetail(
        nowCalories = nowCalories.orZero(),
        nowDistance = nowDistance.orZero(),
        nowSteps = nowSteps.orZero(),
        targetSteps = targetSteps ?: 6_000,
        award = award.orEmpty()
    )
}