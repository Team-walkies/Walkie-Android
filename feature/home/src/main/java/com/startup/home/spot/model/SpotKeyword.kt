package com.startup.home.spot.model

import androidx.annotation.DrawableRes
import com.startup.home.R

enum class SpotKeyword(@DrawableRes val drawableResId: Int) {
    NATURE(R.drawable.ic_map_park), // 자연
    HUMANITIES(R.drawable.ic_map_target), // 인문
    SPORTS(R.drawable.ic_map_sports), // 레포츠
    SHOPPING(R.drawable.ic_map_shopping), // 쇼핑
    FOOD(R.drawable.ic_map_food) // 음식
}