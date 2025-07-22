package com.startup.model.healthcare

import com.startup.common.extension.toAnnotatedString
import com.startup.domain.model.healthcare.DailyHealthcareDetail

data class DailyHealthcareDetailModel(
    val caloriesDisplayModel: CaloriesDisplayModel,
    val nowCalories: Int,
    val nowDistance: Int,
    val nowSteps: Int,
    val targetSteps: Int
) {
    companion object {
        fun DailyHealthcareDetail.toUiModel(): DailyHealthcareDetailModel = DailyHealthcareDetailModel(
            caloriesDisplayModel = CaloriesDisplayModel(
                description = caloriesDescription,
                title = caloriesName.toAnnotatedString(),
                url = caloriesUrl
            ),
            nowCalories = nowCalories,
            nowDistance = nowDistance,
            nowSteps = nowSteps,
            targetSteps = nowSteps
        )
    }
}