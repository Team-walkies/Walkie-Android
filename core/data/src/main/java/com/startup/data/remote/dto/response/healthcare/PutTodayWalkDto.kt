package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.healthcare.TodayWalk

data class PutTodayWalkDto(
    @SerializedName("nowCalories")
    val nowCalories: Int?,
    @SerializedName("nowDistance")
    val nowDistance: Int?,
    @SerializedName("nowSteps")
    val nowSteps: Int?,
    @SerializedName("targetSteps")
    val targetSteps: Int?
) {
    // 나중에 필요하면 사용
    fun toDomain(): TodayWalk = TodayWalk(
        nowCalories = nowCalories.orZero(),
        nowDistance = nowDistance.orZero(),
        nowSteps = nowSteps.orZero(),
        targetSteps = targetSteps.orZero()
    )
}