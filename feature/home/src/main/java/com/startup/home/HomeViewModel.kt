package com.startup.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.base.State
import com.startup.common.event.EggHatchingEvent
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.SpotRepository
import com.startup.stepcounter.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stepCounter: StepCounterService,
    private val dataStore: StepDataStore,
    private val spotRepository: SpotRepository
) : BaseViewModel() {
    private val _state = mutableStateOf(StepCounterState())
    override val state: State
        get() = _state.value


    private val _hatchingInfo = MutableStateFlow<Boolean?>(null)
    val hatchingInfo: StateFlow<Boolean?> = _hatchingInfo

    fun onHatchingAnimationDismissed() {
        _hatchingInfo.value = null
    }

    init {
        viewModelScope.launch {
            stepCounter.observeSteps()
                .collect { steps ->
                    _state.value = StepCounterState(steps = steps)
                }
        }
        viewModelScope.launch {
            EggHatchingEvent.hatchingAnimationFlow.collect { info ->
                _hatchingInfo.value = info
            }
        }
        // todo fetching or 로컬에 저장
        viewModelScope.launch {
            dataStore.setTargetStep(target = 100)
        }
    }

    fun startCounting() {
        stepCounter.startCounting()
    }

    fun stopCounting() {
        stepCounter.stopCounting()
    }

    fun resetStepCount() {
        viewModelScope.launch(Dispatchers.IO) {
            stepCounter.resetSteps()
        }
    }

    fun initTodayStep() {
        viewModelScope.launch {
            val previousSteps = stepCounter.checkAndResetForNewDay()

            // 이전 걸음 수가 있으면 업로드 및 UI 업데이트
            previousSteps?.let { steps ->
                uploadYesterdaySteps(steps)
            }
        }
    }

    private fun uploadYesterdaySteps(steps: Int) {
        // todo 실제 usecase로 핸들링
//        uploadStepUseCase.executeOnViewModel(
//            params = steps,
//            onEach = {
//                // 업로드 성공 시 UI 업데이트
//                updateMainActivity()
//            },
//            onError = { error ->
//                // 에러 처리
//            }
//        ).launchIn(viewModelScope)
    }

    private fun updateMainActivity() {
        // todo view 업데이트
    }
}

data class StepCounterState(
    val steps: Int = 0
) : State