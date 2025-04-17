package com.startup.home.character

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.domain.usecase.GetCurrentHatchedCharacter
import com.startup.domain.usecase.UpdateWalkingCharacter
import com.startup.home.character.model.HatchingCharacterViewModelEvent
import com.startup.home.character.model.HatchingCharacterViewState
import com.startup.home.character.model.HatchingCharacterViewStateImpl
import com.startup.home.character.model.WalkieCharacter
import com.startup.home.character.model.WalkieCharacter.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HatchingCharacterViewModel @Inject constructor(
    getCurrentHatchedCharacter: GetCurrentHatchedCharacter,
    private val updateWalkingCharacter: UpdateWalkingCharacter
) :
    BaseViewModel() {
    private val _state: HatchingCharacterViewStateImpl = HatchingCharacterViewStateImpl(
        jellyfishCharacterList = merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == HatchingCharacterViewModelEvent.FetchCharacterList }
        ).flatMapLatest {
            getCurrentHatchedCharacter
                .invoke(0)
                .map { it.toUiModel() }
                .onEach { Printer.d("LMH", "Jelly $it") }
                .catch { }

        }.stateInViewModel(emptyList()),
        dinoCharacterList = merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == HatchingCharacterViewModelEvent.FetchCharacterList }
        ).flatMapLatest {
            getCurrentHatchedCharacter
                .invoke(1)
                .map { it.toUiModel() }
                .onEach { Printer.d("LMH", "Dino $it") }
                .catch { }
        }.stateInViewModel(emptyList())
    )
    override val state: HatchingCharacterViewState get() = _state

    fun onSelectPartner(character: WalkieCharacter) {
        updateWalkingCharacter
            .invoke(character.characterId)
            .onEach { notifyViewModelEvent(HatchingCharacterViewModelEvent.FetchCharacterList) }
            .catch { }
            .launchIn(viewModelScope)
    }
}