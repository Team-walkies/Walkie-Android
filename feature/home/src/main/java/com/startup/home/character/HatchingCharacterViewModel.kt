package com.startup.home.character

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.BaseUiState
import com.startup.common.util.Printer
import com.startup.domain.usecase.GetCurrentHatchedCharacter
import com.startup.domain.usecase.GetHatchedCharacterDetail
import com.startup.domain.usecase.UpdateWalkingCharacter
import com.startup.home.character.model.HatchingCharacterViewModelEvent
import com.startup.home.character.model.HatchingCharacterViewState
import com.startup.home.character.model.HatchingCharacterViewStateImpl
import com.startup.home.character.model.WalkieCharacter
import com.startup.home.character.model.WalkieCharacter.Companion.toUiModel
import com.startup.home.character.model.WalkieCharacterDetail
import com.startup.home.character.model.WalkieCharacterDetail.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HatchingCharacterViewModel @Inject constructor(
    getCurrentHatchedCharacter: GetCurrentHatchedCharacter,
    private val getHatchedCharacterDetail: GetHatchedCharacterDetail,
    private val updateWalkingCharacter: UpdateWalkingCharacter,
) :
    BaseViewModel() {
    override val state: HatchingCharacterViewState get() = _state
    private val _state: HatchingCharacterViewStateImpl = HatchingCharacterViewStateImpl(
        jellyfishCharacterState = merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == HatchingCharacterViewModelEvent.FetchCharacterList }
        ).flatMapLatest {
            getCurrentHatchedCharacter
                .invoke(0)
                .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                .onEach { Printer.d("LMH", "Jelly $it") }
                .catch { }

        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = emptyList())),
        dinoCharacterState = merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == HatchingCharacterViewModelEvent.FetchCharacterList }
        ).flatMapLatest {
            getCurrentHatchedCharacter
                .invoke(1)
                .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                .onEach { Printer.d("LMH", "Dino $it") }
                .catch { }
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = emptyList())),
        characterDetail = MutableStateFlow(null),
    )

    fun clearViewingPartner() {
        _state.characterDetail.update { null }
    }

    fun onClickPartner(character: WalkieCharacter) {
        getHatchedCharacterDetail
            .invoke(character.characterId)
            .map { it.toUiModel() }
            .onEach { item ->
                _state.characterDetail.update { item } }
            .catch { }
            .launchIn(viewModelScope)
    }

    fun onSelectPartner(characterDetail: WalkieCharacterDetail) {
        clearViewingPartner()
        updateWalkingCharacter
            .invoke(characterDetail.character.characterId)
            .onEach { notifyViewModelEvent(HatchingCharacterViewModelEvent.FetchCharacterList) }
            .catch { }
            .launchIn(viewModelScope)
    }
}