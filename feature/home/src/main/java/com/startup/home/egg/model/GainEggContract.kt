package com.startup.home.egg.model

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.UiEvent
import kotlinx.coroutines.flow.StateFlow

class GainEggViewStateImpl(
    override val eggList: StateFlow<List<MyEggModel>>,
) : GainEggViewState {
}

interface GainEggViewState : BaseState {
    val eggList: StateFlow<List<MyEggModel>>
}

sealed interface GainEggViewModelEvent : BaseEvent {
    data object FetchEggList : GainEggViewModelEvent
}

sealed interface GainEggUiEvent : UiEvent {
    data class OnChangedClickWalkEgg(val eggId: Long) : GainEggUiEvent
}