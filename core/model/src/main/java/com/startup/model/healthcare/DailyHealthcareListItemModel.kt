package com.startup.model.healthcare

import com.startup.common.util.DateUtil
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import java.time.LocalDate

data class DailyHealthcareListItemModel(
    val nowSteps: Int,
    val date: LocalDate,
    val targetSteps: Int
) {
    companion object {
        fun DailyHealthcareListItem.toUiModel() = DailyHealthcareListItemModel(
            nowSteps = nowSteps,
            date = date,
            targetSteps = targetSteps
        )

        fun orEmpty(): DailyHealthcareListItemModel = DailyHealthcareListItemModel(
            nowSteps = 0,
            date = LocalDate.now(),
            targetSteps = 0
        )
    }
}
