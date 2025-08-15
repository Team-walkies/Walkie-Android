package com.startup.model.healthcare

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.startup.model.R

enum class CaloriesType(
    @StringRes val description: Int,
    @StringRes val objectNameStrResId: Int,
    @DrawableRes val imageResId: Int,
) {
    Air(
        description = R.string.healthcare_calories_type_air_description,
        objectNameStrResId = R.string.healthcare_calories_type_air_name,
        imageResId = R.drawable.ic_food_air
    ),
    Candy(
        description = R.string.healthcare_calories_type_candy_description,
        objectNameStrResId = R.string.healthcare_calories_type_candy_name,
        imageResId = R.drawable.ic_food_candy
    ),
    Banana(
        description = R.string.healthcare_calories_type_banana_description,
        objectNameStrResId = R.string.healthcare_calories_type_banana_name,
        imageResId = R.drawable.ic_food_banana
    ),
    TriangleGimbap(
        description = R.string.healthcare_calories_type_triangle_gimbap_description,
        objectNameStrResId = R.string.healthcare_calories_type_triangle_gimbap_name,
        imageResId = R.drawable.ic_food_triangle_gimbap
    ),
    ChickenLeg(
        description = R.string.healthcare_calories_type_chicken_leg_description,
        objectNameStrResId = R.string.healthcare_calories_type_chicken_leg_name,
        imageResId = R.drawable.ic_food_chicken_leg
    ),
    Pasta(
        description = R.string.healthcare_calories_type_pasta_description,
        objectNameStrResId = R.string.healthcare_calories_type_pasta_name,
        imageResId = R.drawable.ic_food_pasta
    );

    companion object {
        fun getStepOfCaloriesType(nowStep: Int): CaloriesType {
            return when (nowStep) {
                in 0..199 -> Air
                in 200..1_999 -> Candy
                in 2_000..3_999 -> Banana
                in 4_000..5_999 -> TriangleGimbap
                in 6_000..7_999 -> ChickenLeg
                else -> {
                    Pasta
                }
            }
        }

        fun orEmpty(): CaloriesType = Air
    }
}