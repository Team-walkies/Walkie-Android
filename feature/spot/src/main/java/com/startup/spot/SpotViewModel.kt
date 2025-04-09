package com.startup.spot

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.domain.usecase.GetSpotWebViewParams
import com.startup.domain.usecase.LocalLogout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SpotViewModel @Inject constructor(
    getSpotWebViewParams: GetSpotWebViewParams,
    private val logout: LocalLogout,
) :
    BaseViewModel() {
    override val state: BaseState = object : BaseState {}

    init {
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
                    notifyEvent(SpotNavigationEvent.FinishSpotActivity)
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
                    // TODO @IslandOrDream 스팟 탐험 완료 후 걸음 수 데이터 전달이 필요하여 요청, 이후 걸음 수 측정 종료
                    notifyEvent(SpotEvent.RequestCurrentSteps(2))
                }

                SpotUiEvent.StartCountingSteps -> {
                    // TODO @IslandOrDream 걸음 수 측정 시작
                }
            }
        }
    }
}