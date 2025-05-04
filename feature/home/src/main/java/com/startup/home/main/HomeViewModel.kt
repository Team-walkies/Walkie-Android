package com.startup.home.main

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.base.UiEvent
import com.startup.common.event.EggHatchingEvent
import com.startup.common.util.BaseUiState
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
import com.startup.domain.usecase.GetRecordedSpotCount
import com.startup.domain.usecase.UpdateEggOfStepCount
import com.startup.home.HomeScreenViewModelEvent
import com.startup.home.character.model.WalkieCharacter
import com.startup.home.character.model.WalkieCharacter.Companion.toUiModel
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.MyEggModel
import com.startup.home.egg.model.MyEggModel.Companion.toUiModel
import com.startup.stepcounter.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
    getCurrentRecordedSpotCount: GetRecordedSpotCount,
    getCurrentWalkCharacter: GetCurrentWalkCharacter,
) : BaseViewModel() {

    private val _showActivityPermissionAlert = MutableStateFlow(false)
    private val _showBackgroundLocationPermissionAlert = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state = HomeViewStateImpl(
        stepsUiState = stepCounter.observeSteps()
            .map { BaseUiState(isShowShimmer = false, data = it) }
            .stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        currentGainEggCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshReviewList }.flatMapLatest {
            getGainEggCount.invoke(Unit)
                .map { BaseUiState(isShowShimmer = false, data = it) }
                .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        currentHatchedCharacterCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshReviewList }.flatMapLatest {
            getHatchedCharacterCount.invoke(Unit)
                .map { BaseUiState(isShowShimmer = false, data = it) }
                .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        // 탐험한 리뷰 수 조회 용
        currentRecordedSpotCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshReviewList }.flatMapLatest {
            getCurrentRecordedSpotCount.invoke(Unit)
                .map { BaseUiState(isShowShimmer = false, data = it) }
                .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
        }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        currentWalkEggUiState = getCurrentWalkEgg.invoke(Unit)
            .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
            .catch {
                emit(
                    BaseUiState(
                        isShowShimmer = false,
                        data = MyEggModel(
                            characterId = 0,
                            eggKind = EggKind.Empty,
                            obtainedDate = "",
                            obtainedPosition = "",
                            eggId = 0,
                            play = false,
                            nowStep = 0,
                            needStep = 0
                        )
                    )
                )
            }
            .stateInViewModel(
                BaseUiState(
                    isShowShimmer = true,
                    data = MyEggModel(
                        characterId = 0,
                        eggKind = EggKind.Empty,
                        obtainedDate = "",
                        obtainedPosition = "",
                        eggId = 0,
                        play = false,
                        nowStep = 0,
                        needStep = 0
                    )
                )
            ),

        currentWalkCharacterUiState = getCurrentWalkCharacter.invoke(Unit)
            .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
            .catch { emit(BaseUiState(isShowShimmer = false, data = WalkieCharacter.ofEmpty())) }
            .stateInViewModel(BaseUiState(isShowShimmer = true, data = WalkieCharacter.ofEmpty())),
        showActivityPermissionAlert = _showActivityPermissionAlert.stateInViewModel(false),
        showBackgroundPermissionAlert = _showBackgroundLocationPermissionAlert.stateInViewModel(false),
        userInfo = getMyData.invoke(Unit).catch {  }.stateInViewModel(null)

    )

    override val state: HomeViewState
        get() = _state

    private val _uiEventFlow = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 10,
        replay = 1

    )
    val uiEventFlow: SharedFlow<UiEvent> = _uiEventFlow.asSharedFlow()

    private val _hatchingInfo = MutableStateFlow(
        BaseUiState(
            isShowShimmer = false,
            data = HatchingAnimationCharacterData()
        )
    )
    val hatchingInfo: StateFlow<BaseUiState<HatchingAnimationCharacterData>> = _hatchingInfo


    fun onHatchingAnimationDismissed() {
        _hatchingInfo.value = BaseUiState(
            isShowShimmer = false,
            data = HatchingAnimationCharacterData(isHatching = false)
        )
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
                    _hatchingInfo.value = BaseUiState(
                        isShowShimmer = false,
                        data = HatchingAnimationCharacterData(
                            isHatching = true,
                            character = _state.currentWalkCharacterUiState.value.data,
                            eggKind = _state.currentWalkEggUiState.value.data.eggKind
                        )
                    )
                    processUserInfo()
                } else {
                    _hatchingInfo.value = BaseUiState(
                        isShowShimmer = false,
                        data = HatchingAnimationCharacterData(isHatching = false)
                    )
                }
            }
        }
    }

    private fun processUserInfo() {
        viewModelScope.launch {
            _state.userInfo
                .collect { userInfo ->
                    if(userInfo != null) {
                        Printer.e("JUNWOO", "eggId : ${userInfo.eggId}")
                        updateStepProgress(userInfo)
                    }
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
                ).onEach {
                    // 알 부화 처리 이후 홈 리프레시
                    viewModelScope.launch {
                        notifyViewModelEvent(HomeScreenViewModelEvent.RefreshReviewList)
                    }
                }.catch {}.launchIn(viewModelScope)

                resetStepCount()
            }
        }
    }

    private fun setupTargetSteps() {
        viewModelScope.launch {
            _state.currentWalkEggUiState
                .filter { it.data.needStep > 0 }
                .take(1)
                .collect { currentEgg ->
                    dataStore.setTargetStep(target = currentEgg.data.needStep)
                    Printer.e("JUNWOO", "Target step set: ${currentEgg.data.needStep}")
                }
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

    fun setBackgroundLocationPermssionAlertState(show: Boolean) {
        _showBackgroundLocationPermissionAlert.value = show
    }
}

interface HomeViewState : BaseState {
    val stepsUiState: StateFlow<BaseUiState<Int>>
    val currentHatchedCharacterCountUiState: StateFlow<BaseUiState<Int>>
    val currentGainEggCountUiState: StateFlow<BaseUiState<Int>>
    val currentRecordedSpotCountUiState: StateFlow<BaseUiState<Int>>
    val currentWalkEggUiState: StateFlow<BaseUiState<MyEggModel>>
    val currentWalkCharacterUiState: StateFlow<BaseUiState<WalkieCharacter>>
    val showActivityPermissionAlert: StateFlow<Boolean>
    val showBackgroundPermissionAlert: StateFlow<Boolean>
    val userInfo: StateFlow<UserInfo?>
}

class HomeViewStateImpl(
    override val stepsUiState: StateFlow<BaseUiState<Int>>,
    override val currentHatchedCharacterCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentGainEggCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentRecordedSpotCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentWalkEggUiState: StateFlow<BaseUiState<MyEggModel>>,
    override val currentWalkCharacterUiState: StateFlow<BaseUiState<WalkieCharacter>>,
    override val showActivityPermissionAlert: StateFlow<Boolean>,
    override val showBackgroundPermissionAlert: StateFlow<Boolean>,
    override val userInfo: StateFlow<UserInfo?>
) : HomeViewState

data class HatchingAnimationCharacterData(
    val isHatching: Boolean = false,
    val character: WalkieCharacter = WalkieCharacter.ofEmpty(),
    val eggKind: EggKind = EggKind.Empty
)