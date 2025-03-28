package com.startup.home.main

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.base.UiEvent
import com.startup.common.event.EggHatchingEvent
import com.startup.domain.model.member.UserInfo
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.SpotRepository
import com.startup.domain.usecase.GetGainEggCount
import com.startup.domain.usecase.GetHatchedCharacterCount
import com.startup.domain.usecase.GetMyData
import com.startup.stepcounter.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stepCounter: StepCounterService,
    private val dataStore: StepDataStore,
    getGainEggCount: GetGainEggCount,
    getHatchedCharacterCount: GetHatchedCharacterCount,
    getMyData: GetMyData,
    private val spotRepository: SpotRepository
) : BaseViewModel() {

    private val _showActivityPermissionAlert = MutableStateFlow(false)

    private val _state = HomeViewStateImpl(
        steps = stepCounter.observeSteps()
            .stateInViewModel(0),
        currentGainEggCount =
        getGainEggCount.invoke(Unit)
            .catch {

            }.stateInViewModel(0),
        currentHatchedCharacterCount = getHatchedCharacterCount.invoke(Unit)
            .catch {

            }.stateInViewModel(0),
        userInfo = getMyData.invoke(Unit)
            .catch {

            }.stateInViewModel(null),
        showActivityPermissionAlert = _showActivityPermissionAlert.stateInViewModel(false)
    )
    override val state: HomeViewState
        get() = _state

    private val _uiEventFlow = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 10,
        replay = 1

    )
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _hatchingInfo = MutableStateFlow<Boolean?>(null)
    val hatchingInfo: StateFlow<Boolean?> = _hatchingInfo

    fun onHatchingAnimationDismissed() {
        _hatchingInfo.value = null
    }

    fun emitUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEventFlow.emit(event)
        }
    }

    init {
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

    fun setActivityPermissionAlertState(show: Boolean) {
        _showActivityPermissionAlert.value = show
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

interface HomeViewState : BaseState {
    val steps: StateFlow<Int>
    val currentHatchedCharacterCount: StateFlow<Int>
    val currentGainEggCount: StateFlow<Int>
    val userInfo: StateFlow<UserInfo?>
    val showActivityPermissionAlert: StateFlow<Boolean>
}

class HomeViewStateImpl(
    override val steps: StateFlow<Int>,
    override val currentHatchedCharacterCount: StateFlow<Int>,
    override val currentGainEggCount: StateFlow<Int>,
    override val userInfo: StateFlow<UserInfo?>,
    override val showActivityPermissionAlert: StateFlow<Boolean>
) : HomeViewState