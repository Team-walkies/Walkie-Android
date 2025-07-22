package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class ResponseDailyHealthcareDetail(
    @SerializedName("nowCalories")
    val nowCalories: Int?,
    @SerializedName("nowDistance")
    val nowDistance: Int?,
    @SerializedName("nowSteps")
    val nowSteps: Int?,
    @SerializedName("targetSteps")
    val targetSteps: Int?
) {
    fun toDomain(): DailyHealthcareDetail = DailyHealthcareDetail(
        nowCalories = nowCalories.orZero(),
        nowDistance = nowDistance.orZero().toDouble(),
        nowSteps = nowSteps.orZero(),
        targetSteps = targetSteps ?: 6_000,
    )
}