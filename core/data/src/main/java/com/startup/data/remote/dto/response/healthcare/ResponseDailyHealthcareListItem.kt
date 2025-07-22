package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.common.util.DateUtil
import com.startup.domain.model.healthcare.DailyHealthcareListItem

data class ResponseDailyHealthcareListItem(
    @SerializedName("nowSteps")
    val nowSteps: Int?,
    @SerializedName("responseDate")
    val responseDate: String?,
    @SerializedName("targetSteps")
    val targetSteps: Int?
) {
    fun toDomain(): DailyHealthcareListItem = DailyHealthcareListItem(
        nowSteps = nowSteps.orZero(),
        date = DateUtil.convertLocalDate(responseDate.orEmpty()),
        targetSteps = targetSteps.orZero()
    )
}