package com.startup.home.character.model

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface HatchingCharacterViewState : BaseState {
    val jellyfishCharacterList: StateFlow<List<WalkieCharacter>>
    val dinoCharacterList: StateFlow<List<WalkieCharacter>>
}

class HatchingCharacterViewStateImpl(
    override val jellyfishCharacterList: StateFlow<List<WalkieCharacter>>,
    override val dinoCharacterList: StateFlow<List<WalkieCharacter>>
) : HatchingCharacterViewState


sealed interface HatchingCharacterViewModelEvent : BaseEvent {
    data object FetchCharacterList : HatchingCharacterViewModelEvent
}
