package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class ResponseDailyHealthcareDetail(
    @SerializedName("caloriesDescription")
    val caloriesDescription: String?,
    @SerializedName("caloriesName")
    val caloriesName: String?,
    @SerializedName("caloriesUrl")
    val caloriesUrl: String?,
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
        caloriesDescription.orEmpty(),
        caloriesName.orEmpty(),
        caloriesUrl.orEmpty(),
        nowCalories.orZero(),
        nowDistance.orZero(),
        nowSteps.orZero(),
        targetSteps.orZero(),
    )
}