package com.startup.login.splash

import com.startup.common.base.BaseState
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface SplashUiEvent : UiEvent {
    data object RedirectPlayStore : SplashUiEvent
}

interface SplashViewState : BaseState {
    val isForceUpdateDialogShow: StateFlow<Boolean>
}

class SplashViewStateImpl : SplashViewState {
    override val isForceUpdateDialogShow: MutableStateFlow<Boolean> = MutableStateFlow(false)
}

sealed interface SplashNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : SplashNavigationEvent
    data object MoveToOnBoarding : SplashNavigationEvent
}