package com.startup.home.spot.model

import androidx.annotation.StringRes
import com.startup.home.R

internal enum class WeekendType(@StringRes val weekDisplayStrResId: Int) {
    Sun(R.string.week_sun),
    Mon(R.string.week_mon),
    The(R.string.week_the),
    Wen(R.string.week_wen),
    Thu(R.string.week_thu),
    Fri(R.string.week_fri),
    Sat(R.string.week_sat)
}