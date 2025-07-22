package com.startup.model.healthcare

import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class DailyHealthcareDetailModel(
    val caloriesDisplayModel: CaloriesDisplayModel,
    val nowCalories: Int,
    val nowDistance: Int,
    val nowSteps: Int,
    val targetSteps: Int
) {
    companion object {
        fun orEmpty(): DailyHealthcareDetailModel = DailyHealthcareDetailModel(
            caloriesDisplayModel = CaloriesDisplayModel.orEmpty(),
            nowDistance = 0,
            nowCalories = 0,
            targetSteps = 4000,
            nowSteps = 0
        )

        fun DailyHealthcareDetail.toUiModel(): DailyHealthcareDetailModel {
            return DailyHealthcareDetailModel(
                caloriesDisplayModel = CaloriesDisplayModel(
                    description = caloriesDescription,
                    title = caloriesName,
                    url = caloriesUrl
                ),
                nowCalories = nowCalories,
                nowDistance = nowDistance,
                nowSteps = nowSteps,
                targetSteps = nowSteps
            )
        }
    }
}