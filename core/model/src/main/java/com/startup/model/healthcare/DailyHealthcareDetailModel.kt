package com.startup.model.healthcare

import com.startup.common.extension.safeEnumOf
import com.startup.design_system.widget.badge.EggBadgeStatus
import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class DailyHealthcareDetailModel(
    val caloriesType: CaloriesType,
    val nowCalories: Int,
    val nowDistance: Double,
    val nowSteps: Int,
    val targetSteps: Int,
    val eggBadgeStatus: EggBadgeStatus,
) {
    companion object {
        fun orEmpty(): DailyHealthcareDetailModel = DailyHealthcareDetailModel(
            caloriesType = CaloriesType.orEmpty(),
            nowDistance = 0.0,
            nowCalories = 0,
            targetSteps = 6_000,
            nowSteps = 0,
            eggBadgeStatus = EggBadgeStatus.PENDING
        )

        fun DailyHealthcareDetail.toUiModel(): DailyHealthcareDetailModel {
            return DailyHealthcareDetailModel(
                caloriesType = CaloriesType.getStepOfCaloriesType(nowSteps),
                nowCalories = nowCalories,
                nowDistance = nowDistance,
                nowSteps = nowSteps,
                targetSteps = targetSteps,
                eggBadgeStatus = safeEnumOf(award, EggBadgeStatus.PENDING)
            )
        }
    }
}