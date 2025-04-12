package com.startup.home.main

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.base.UiEvent
import com.startup.common.event.EggHatchingEvent
import com.startup.common.util.Printer
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.model.member.UserInfo
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.LocationRepository
import com.startup.domain.usecase.GetCurrentWalkCharacter
import com.startup.domain.usecase.GetCurrentWalkEgg
import com.startup.domain.usecase.GetGainEggCount
import com.startup.domain.usecase.GetHatchedCharacterCount
import com.startup.domain.usecase.GetMyData
import com.startup.domain.usecase.UpdateEggOfStepCount
import com.startup.home.character.model.WalkieCharacter
import com.startup.home.character.model.WalkieCharacter.Companion.toUiModel
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.MyEggModel
import com.startup.home.egg.model.MyEggModel.Companion.toUiModel
import com.startup.stepcounter.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stepCounter: StepCounterService,
    private val dataStore: StepDataStore,
    private val locationRepository: LocationRepository,
    private val updateEggOfStepCount: UpdateEggOfStepCount,
    getGainEggCount: GetGainEggCount,
    getHatchedCharacterCount: GetHatchedCharacterCount,
    getMyData: GetMyData,
    getCurrentWalkEgg: GetCurrentWalkEgg,
    getCurrentWalkCharacter: GetCurrentWalkCharacter,
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
        currentWalkEgg = getCurrentWalkEgg.invoke(Unit)
            .map { it.toUiModel() }
            .catch { }
            .stateInViewModel(
                MyEggModel(
                    characterId = 0,
                    eggKind = EggKind.Empty,
                    obtainedDate = "",
                    obtainedPosition = "",
                    eggId = 0,
                    play = false,
                    nowStep = 0,
                    needStep = 0
                )
            ),
        currentWalkCharacter = getCurrentWalkCharacter.invoke(Unit)
            .map { it.toUiModel() }
            .catch { }
            .stateInViewModel(WalkieCharacter.ofEmpty()),
        showActivityPermissionAlert = _showActivityPermissionAlert.stateInViewModel(false)
    )
    override val state: HomeViewState
        get() = _state

    private val _uiEventFlow = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 10,
        replay = 1

    )
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _hatchingInfo = MutableStateFlow<HatchingAnimationCharacterData?>(null)
    val hatchingInfo: StateFlow<HatchingAnimationCharacterData?> = _hatchingInfo

    fun onHatchingAnimationDismissed() {
        _hatchingInfo.value = null
    }

    fun emitUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEventFlow.emit(event)
        }
    }

    init {
        observeEggHatchingEvents()
        processUserInfo()
        setupTargetSteps()
    }

    private fun observeEggHatchingEvents() {
        viewModelScope.launch {
            EggHatchingEvent.hatchingAnimationFlow.collect { isHatching ->
                if (isHatching) {
                    _hatchingInfo.value = HatchingAnimationCharacterData(
                        isHatching = true,
                        character = _state.currentWalkCharacter.value,
                        eggKind = _state.currentWalkEgg.value.eggKind
                    )
                } else {
                    _hatchingInfo.value = null
                }
            }
        }
    }

    private fun processUserInfo() {
        viewModelScope.launch {
            _state.userInfo
                .filterNotNull()
                .take(1)
                .catch { }
                .collect { userInfo ->
                    Printer.e("JUNWOO", "eggId : ${userInfo.eggId}")
                    updateStepProgress(userInfo)
                }
        }
    }

    private fun updateStepProgress(userInfo: UserInfo) {
        viewModelScope.launch {
            val remainingStep = dataStore.getTargetStep() - dataStore.getCurrentSteps()

            if (remainingStep > 0) {
                // 남은 걸음수가 있는 경우
                Printer.e("JUNWOO", "remainingStep : $remainingStep")
                updateStepWithStepCount(userInfo.eggId, remainingStep)
            } else {
                // 목표 달성한 경우 - 위치 정보 포함하여 업데이트
                updateEggWithLocationData(userInfo.eggId, remainingStep)
            }
        }
    }

    private fun updateStepWithStepCount(eggId: Long, steps: Int) {
        updateEggOfStepCount.invoke(
            UpdateStepData(
                eggId = eggId,
                nowStep = steps
            )
        ).catch {}.launchIn(viewModelScope)
    }

    private fun updateEggWithLocationData(eggId: Long, steps: Int) {
        viewModelScope.launch {
            locationRepository.getCurrentLocation().collect { locationData ->
                Printer.e(
                    "JUNWOO",
                    "latitude : ${locationData.latitude} , longitude : ${locationData.longitude}"
                )

                updateEggOfStepCount.invoke(
                    UpdateStepData(
                        eggId = eggId,
                        nowStep = steps,
                        latitude = locationData.latitude,
                        longitude = locationData.longitude
                    )
                ).catch {}.launchIn(viewModelScope)

                resetStepCount()
            }
        }
    }

    private fun setupTargetSteps() {
        // todo fetching or 로컬에 저장
        viewModelScope.launch {
            dataStore.setTargetStep(target = 20)
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
}

interface HomeViewState : BaseState {
    val steps: StateFlow<Int>
    val currentHatchedCharacterCount: StateFlow<Int>
    val currentGainEggCount: StateFlow<Int>
    val userInfo: StateFlow<UserInfo?>
    val currentWalkEgg: StateFlow<MyEggModel>
    val currentWalkCharacter: StateFlow<WalkieCharacter>
    val showActivityPermissionAlert: StateFlow<Boolean>
}

class HomeViewStateImpl(
    override val steps: StateFlow<Int>,
    override val currentHatchedCharacterCount: StateFlow<Int>,
    override val currentGainEggCount: StateFlow<Int>,
    override val userInfo: StateFlow<UserInfo?>,
    override val currentWalkEgg: StateFlow<MyEggModel>,
    override val currentWalkCharacter: StateFlow<WalkieCharacter>,
    override val showActivityPermissionAlert: StateFlow<Boolean>
) : HomeViewState

data class HatchingAnimationCharacterData(
    val isHatching: Boolean,
    val character: WalkieCharacter? = null,
    val eggKind: EggKind = EggKind.Empty
)