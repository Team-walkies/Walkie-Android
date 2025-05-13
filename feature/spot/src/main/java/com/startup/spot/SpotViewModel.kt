package com.startup.spot

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.domain.provider.StepDataStore
import com.startup.domain.usecase.spot.GetSpotWebViewParams
import com.startup.domain.usecase.auth.LocalLogout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotViewModel @Inject constructor(
    private val getSpotWebViewParams: GetSpotWebViewParams,
    private val logout: LocalLogout,
    private val stepDataStore: StepDataStore
) :
    BaseViewModel() {
    override val state: BaseState = object : BaseState {}

    private var spotStepStartValue = 0

    private fun fetchLoadUrlParams() {
        getSpotWebViewParams.invoke(Unit)
            .onEach {
                Printer.e("LMH", "GET $it")
                notifyEvent(SpotEvent.LoadWebView(it))
            }
            .catch { Printer.e("LMH", "CATCH $it") }.launchIn(viewModelScope)
    }

    override fun handleViewModelEvent(event: BaseEvent) {
        if (event is SpotUiEvent) {
            when (event) {
                SpotUiEvent.Haptic -> {
                    notifyEvent(SpotEvent.Haptic)
                }

                SpotUiEvent.FinishReview -> {
                    notifyEvent(SpotNavigationEvent.FinishAndRefreshSpotActivity)
                }

                SpotUiEvent.FinishWebView -> {
                    notifyEvent(SpotNavigationEvent.FinishSpotActivity)
                }

                SpotUiEvent.Logout -> {
                    logout
                        .invoke(Unit)
                        .onEach { notifyEvent(SpotNavigationEvent.Logout) }
                        .catch { }
                        .launchIn(viewModelScope)
                }

                SpotUiEvent.PressBackBtn -> {
                    notifyEvent(SpotNavigationEvent.FinishSpotActivity)
                }

                SpotUiEvent.RequestCurrentSteps -> {
                    // 스팟 탐험 완료 후 현재까지 증가한 걸음수를 계산하여 전달
                    viewModelScope.launch {
                        val currentSteps = stepDataStore.getEggCurrentSteps()
                        val spotStepsDifference = currentSteps - spotStepStartValue
                        notifyEvent(SpotEvent.RequestCurrentSteps(spotStepsDifference))
                    }
                }

                SpotUiEvent.StartCountingSteps -> {
                    // 스팟 탐험 걸음수 측정 시작 - 현재 걸음수를 기준점으로 저장
                    viewModelScope.launch {
                        spotStepStartValue = stepDataStore.getEggCurrentSteps()
                    }
                }
                SpotUiEvent.LoadWebViewParams -> {
                    fetchLoadUrlParams()
                }
            }
        }
    }
}