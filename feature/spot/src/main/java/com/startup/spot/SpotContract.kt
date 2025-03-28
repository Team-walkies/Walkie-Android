package com.startup.spot

import com.startup.common.base.BaseEvent
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.domain.model.spot.SpotWebPostRequest

sealed interface SpotEvent : BaseEvent {
    data class LoadWebView(val spotWebPostRequestData: SpotWebPostRequest) : SpotEvent
    data object Haptic : SpotEvent
    data class RequestCurrentSteps(val steps: Int) : SpotEvent
}

sealed interface SpotNavigationEvent : NavigationEvent {
    data object FinishSpotActivity: SpotNavigationEvent
    data object Logout: SpotNavigationEvent
}

sealed interface SpotUiEvent : UiEvent {
    data object Haptic : SpotUiEvent
    data object StartCountingSteps : SpotUiEvent
    data object PressBackBtn : SpotUiEvent
    data object FinishReview : SpotUiEvent
    data object Logout : SpotUiEvent
    data object FinishWebView : SpotUiEvent
    data object RequestCurrentSteps : SpotUiEvent
}