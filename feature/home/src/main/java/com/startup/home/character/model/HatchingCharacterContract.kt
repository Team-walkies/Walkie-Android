package com.startup.home.character.model

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.util.BaseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface HatchingCharacterViewState : BaseState {
    val jellyfishCharacterState: StateFlow<BaseUiState<List<WalkieCharacter>>>
    val dinoCharacterState: StateFlow<BaseUiState<List<WalkieCharacter>>>
    val characterDetail : StateFlow<WalkieCharacterDetail?>
}

class HatchingCharacterViewStateImpl(
    override val jellyfishCharacterState: StateFlow<BaseUiState<List<WalkieCharacter>>>,
    override val dinoCharacterState: StateFlow<BaseUiState<List<WalkieCharacter>>>,
    override val characterDetail: MutableStateFlow<WalkieCharacterDetail?>,
) : HatchingCharacterViewState


sealed interface HatchingCharacterViewModelEvent : BaseEvent {
    data object FetchCharacterList : HatchingCharacterViewModelEvent
}
