package com.startup.home.egg

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent
import com.startup.model.egg.MyEggModel
import com.startup.common.base.BaseUiState
import kotlinx.coroutines.flow.StateFlow

class GainEggViewStateImpl(
    override val eggUiState: StateFlow<BaseUiState<List<MyEggModel>>>,
) : GainEggViewState

interface GainEggViewState : BaseState {
    val eggUiState: StateFlow<BaseUiState<List<MyEggModel>>>
}

sealed interface GainEggViewModelEvent : BaseEvent {
    data object FetchEggList : GainEggViewModelEvent
}

sealed interface GainEggUiEvent : UiEvent {
    data class OnChangedClickWalkEgg(val eggId: Long, val needStep: Int, val nowStep: Int) :
        GainEggUiEvent
}

sealed interface GainEggScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToEggGainProbabilityScreen : GainEggScreenNavigationEvent
    data object Back : GainEggScreenNavigationEvent
}