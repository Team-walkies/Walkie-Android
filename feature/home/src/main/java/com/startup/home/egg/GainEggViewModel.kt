package com.startup.home.egg

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.BaseUiState
import com.startup.domain.usecase.GetGainEggList
import com.startup.domain.usecase.UpdateWalkingEgg
import com.startup.home.egg.model.GainEggViewModelEvent
import com.startup.home.egg.model.GainEggViewState
import com.startup.home.egg.model.GainEggViewStateImpl
import com.startup.home.egg.model.MyEggModel.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
class GainEggViewModel @Inject constructor(
    getGainEggList: GetGainEggList,
    private val updateWalkingEgg: UpdateWalkingEgg
) : BaseViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state = GainEggViewStateImpl(
        merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == GainEggViewModelEvent.FetchEggList }
        ).flatMapLatest {
            getGainEggList
                .invoke(Unit)
                .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                .catch {}
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = emptyList()))
    )
    override val state: GainEggViewState get() = _state

    fun updateEgg(eggId: Long) {
        updateWalkingEgg.invoke(eggId)
            .onEach {
                notifyViewModelEvent(GainEggViewModelEvent.FetchEggList)
            }.catch {

            }.launchIn(viewModelScope)
    }
}