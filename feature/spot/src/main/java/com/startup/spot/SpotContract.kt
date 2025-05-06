package com.startup.spot

import com.startup.common.base.BaseEvent
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.domain.model.spot.ModifyReviewWebPostRequest
import com.startup.domain.model.spot.SpotWebPostRequest

sealed interface SpotEvent : BaseEvent {
    data class LoadWebView(val spotWebPostRequestData: SpotWebPostRequest) : SpotEvent
    data object Haptic : SpotEvent
    data class RequestCurrentSteps(val steps: Int) : SpotEvent
}

sealed interface SpotNavigationEvent : NavigationEvent {
    data object FinishSpotActivity: SpotNavigationEvent
    data object FinishAndRefreshSpotActivity: SpotNavigationEvent
    data object Logout: SpotNavigationEvent
}

sealed interface SpotUiEvent : UiEvent {
    data object LoadWebViewParams : SpotUiEvent
    data object Haptic : SpotUiEvent
    data object StartCountingSteps : SpotUiEvent
    data object PressBackBtn : SpotUiEvent
    data object FinishReview : SpotUiEvent
    data object Logout : SpotUiEvent
    data object FinishWebView : SpotUiEvent
    data object RequestCurrentSteps : SpotUiEvent
}

sealed interface ModifyReviewEvent : BaseEvent {
    data class LoadWebView(val modifyReviewWebPostRequest: ModifyReviewWebPostRequest) : ModifyReviewEvent
}

sealed interface ModifyReviewUiEvent : UiEvent {
    data object LoadWebViewParams : ModifyReviewUiEvent
    data object FinishReviewModify : ModifyReviewUiEvent
    data object Logout : ModifyReviewUiEvent
    data object FinishWebView : ModifyReviewUiEvent
}

sealed interface ModifyReviewNavigationEvent : NavigationEvent {
    data object FinishWithModifyActivity: ModifyReviewNavigationEvent
    data object Finish: ModifyReviewNavigationEvent
    data object Logout: ModifyReviewNavigationEvent
}