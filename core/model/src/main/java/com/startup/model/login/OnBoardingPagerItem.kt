package com.startup.model.login

import androidx.annotation.DrawableRes

data class OnBoardingPagerItem(
    @DrawableRes val guideDrawableResId: Int,
    val title: String,
    val description: String
)