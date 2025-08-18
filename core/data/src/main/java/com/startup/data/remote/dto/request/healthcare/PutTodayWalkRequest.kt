package com.startup.data.remote.dto.request.healthcare

import com.google.gson.annotations.SerializedName

data class PutTodayWalkRequest(
    @SerializedName("nowCalories")
    val nowCalories: Int,
    @SerializedName("nowDay")
    val nowDay: String,
    @SerializedName("nowDistance")
    val nowDistance: Double,
    @SerializedName("nowSteps")
    val nowSteps: Int,
    @SerializedName("targetSteps")
    val targetSteps: Int
)