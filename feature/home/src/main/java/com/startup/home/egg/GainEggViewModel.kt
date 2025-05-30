package com.startup.home.egg

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseUiState
import com.startup.common.base.BaseViewModel
import com.startup.common.event.EventContainer
import com.startup.common.event.EventContainer.triggerNotificationUpdate
import com.startup.common.util.Printer
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.provider.StepDataStore
import com.startup.domain.usecase.egg.GetGainEggList
import com.startup.domain.usecase.egg.UpdateEggOfStepCount
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GainEggViewModel @Inject constructor(
    getGainEggList: GetGainEggList,
    private val updateWalkingEgg: UpdateWalkingEgg,
    private val updateEggOfStepCount: UpdateEggOfStepCount,
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
        viewModelScope.launch {
            val currentWalkEggId = dataStore.getCurrentWalkEggId()
            if (currentWalkEggId != 0L && currentWalkEggId != eggId) {
                val currentSteps = dataStore.getEggCurrentSteps()

                updateEggOfStepCount.invoke(
                    UpdateStepData(
                        eggId = currentWalkEggId,
                        nowStep = currentSteps
                    )
                ).onCompletion {
                    // 저장 완료 후 새로운 알로 변경
                    updateNewEgg(eggId, needStep, nowStep)
                }.catch {
                    Printer.e("GainEggViewModel", "Failed to save previous egg steps")
                    updateNewEgg(eggId, needStep, nowStep)
                }.launchIn(viewModelScope)
            } else {
                // 이전 알이 없으면 바로 새로운 알로 변경
                updateNewEgg(eggId, needStep, nowStep)
            }
        }
    }

    private fun updateNewEgg(eggId: Long, needStep: Int, nowStep: Int) {
        updateWalkingEgg.invoke(eggId)
            .onEach {
                notifyViewModelEvent(GainEggViewModelEvent.FetchEggList)
                EventContainer.onRefreshEvent()
                if (needStep > 0) {
                    dataStore.setHatchingTargetStep(target = needStep)
                    dataStore.saveEggCurrentSteps(steps = nowStep)
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
