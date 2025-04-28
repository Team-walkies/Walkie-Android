package com.startup.login.login.model

import androidx.annotation.DrawableRes

data class OnBoardingPagerItem(
    @DrawableRes val guideDrawableResId: Int,
    val title: String,
    val description: String
)