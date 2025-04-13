package com.startup.home.egg.model

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.UiEvent
import com.startup.common.util.BaseUiState
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