package com.startup.home.egg

import com.startup.common.base.ScreenNavigationEvent

sealed interface GainEggScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToEggGainProbabilityScreen : GainEggScreenNavigationEvent
    data object Back : GainEggScreenNavigationEvent
}