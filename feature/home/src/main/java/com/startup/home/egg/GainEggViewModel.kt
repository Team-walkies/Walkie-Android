package com.startup.home.egg

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.event.EventContainer
import com.startup.common.util.Printer
import com.startup.domain.provider.StepDataStore
import com.startup.common.base.BaseUiState
import com.startup.common.event.EventContainer.triggerNotificationUpdate
import com.startup.domain.usecase.egg.GetGainEggList
import com.startup.domain.usecase.walk.UpdateWalkingEgg
import com.startup.model.egg.MyEggModel.Companion.toUiModel
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
class GainEggViewModel @Inject constructor(
    getGainEggList: GetGainEggList,
    private val updateWalkingEgg: UpdateWalkingEgg,
    private val dataStore: StepDataStore
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
                .catch { emit(BaseUiState(isShowShimmer = false, data = emptyList())) }
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = emptyList()))
    )
    override val state: GainEggViewState get() = _state

    fun updateEgg(eggId: Long, needStep: Int, nowStep: Int) {
        updateWalkingEgg.invoke(eggId)
            .onEach {
                notifyViewModelEvent(GainEggViewModelEvent.FetchEggList)
                EventContainer.onRefreshEvent()
                if (needStep > 0) {
                    dataStore.setHatchingTargetStep(target = needStep)
                    dataStore.saveEggCurrentSteps(steps = nowStep) // 알이 변경되었으므로 걸음수를 eggModel의 nowStep으로 초기화
                    dataStore.setCurrentWalkEggId(eggId = eggId)
                    triggerNotificationUpdate(targetStep = needStep)
                    Printer.d(
                        "JUNWOO",
                        "Target step set: $needStep, Current steps set to: $nowStep"
                    )
                }
            }.catch {

            }.launchIn(viewModelScope)
    }
}
