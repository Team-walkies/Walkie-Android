package com.startup.walkie.splash

import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent


sealed interface SplashUiEvent : UiEvent {

}

sealed interface SplashNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : SplashNavigationEvent
    data object MoveToOnBoardingAndNickNameSet : SplashNavigationEvent
    data object MoveToOnBoarding : SplashNavigationEvent
}