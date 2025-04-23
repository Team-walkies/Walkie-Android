package com.startup.login.splash

import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent


sealed interface SplashUiEvent : UiEvent {

}

sealed interface SplashNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : SplashNavigationEvent
    data object MoveToOnBoarding : SplashNavigationEvent
}