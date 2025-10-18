package com.startup.model.healthcare

import com.startup.common.extension.safeEnumOf
import com.startup.design_system.widget.badge.EggBadgeStatus
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import java.time.LocalDate

data class DailyHealthcareListItemModel(
    val nowSteps: Int,
    val date: LocalDate,
    val targetSteps: Int,
    val eggBadgeStatus: EggBadgeStatus
) {
    companion object {
        fun DailyHealthcareListItem.toUiModel() = DailyHealthcareListItemModel(
            nowSteps = nowSteps,
            date = date,
            targetSteps = targetSteps,
            eggBadgeStatus = safeEnumOf(award, EggBadgeStatus.PENDING)
        )

        fun orEmpty(): DailyHealthcareListItemModel = DailyHealthcareListItemModel(
            nowSteps = 0,
            date = LocalDate.now(),
            targetSteps = 0,
            eggBadgeStatus = EggBadgeStatus.PENDING
        )
    }
}
