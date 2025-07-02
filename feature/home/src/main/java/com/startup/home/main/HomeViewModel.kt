package com.startup.home.main

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseState
import com.startup.common.base.BaseUiState
import com.startup.common.base.BaseViewModel
import com.startup.common.base.UiEvent
import com.startup.common.event.EventContainer
import com.startup.common.util.Printer
import com.startup.domain.model.egg.UpdateStepData
import com.startup.domain.model.member.UserInfo
import com.startup.domain.provider.StepCounterService
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.LocationRepository
import com.startup.domain.repository.UserRepository
import com.startup.domain.usecase.character.GetHatchedCharacterCount
import com.startup.domain.usecase.egg.GetDailyEgg
import com.startup.domain.usecase.egg.GetGainEggCount
import com.startup.domain.usecase.egg.UpdateEggOfStepCount
import com.startup.domain.usecase.profile.GetMyData
import com.startup.domain.usecase.spot.GetRecordedSpotCount
import com.startup.domain.usecase.walk.GetCurrentWalkCharacter
import com.startup.domain.usecase.walk.GetCurrentWalkEgg
import com.startup.home.HomeScreenViewModelEvent
import com.startup.model.character.WalkieCharacter
import com.startup.model.character.WalkieCharacter.Companion.toUiModel
import com.startup.model.egg.EggKind
import com.startup.model.egg.MyEggModel
import com.startup.model.egg.MyEggModel.Companion.toUiModel
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stepCounterService: StepCounterService,
    private val dataStore: StepDataStore,
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository,
    private val updateEggOfStepCount: UpdateEggOfStepCount,
    private val getCurrentWalkEgg: GetCurrentWalkEgg,
    private val getDailyEgg: GetDailyEgg,
    getGainEggCount: GetGainEggCount,
    getHatchedCharacterCount: GetHatchedCharacterCount,
    getMyData: GetMyData,
    getCurrentRecordedSpotCount: GetRecordedSpotCount,
    getCurrentWalkCharacter: GetCurrentWalkCharacter,
) : BaseViewModel() {

    private val _showActivityPermissionAlert = MutableStateFlow(false)
    private val _showBackgroundLocationPermissionAlert = MutableStateFlow(false)

    // Event UI State
    private val _eventState = MutableStateFlow(EventUiState())
    val eventState: StateFlow<EventUiState> = _eventState

    // Navigation events
    private val _navigateToGainEgg = MutableSharedFlow<Unit>()
    val navigateToGainEgg: SharedFlow<Unit> = _navigateToGainEgg.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state = HomeViewStateImpl(
        stepsUiState = stepCounterService.observeSteps()
            .map { BaseUiState(isShowShimmer = false, data = it) }
            .stateInViewModel(BaseUiState(isShowShimmer = true, data = Pair(0, 0))),

        currentGainEggCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshHome }
            .flatMapLatest {
                getGainEggCount.invoke(Unit)
                    .map { BaseUiState(isShowShimmer = false, data = it) }
                    .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
            }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        currentHatchedCharacterCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshHome }
            .flatMapLatest {
                getHatchedCharacterCount.invoke(Unit)
                    .map { BaseUiState(isShowShimmer = false, data = it) }
                    .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
            }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        // 탐험한 리뷰 수 조회 용
        currentRecordedSpotCountUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshHome }
            .flatMapLatest {
                getCurrentRecordedSpotCount.invoke(Unit)
                    .map { BaseUiState(isShowShimmer = false, data = it) }
                    .catch { emit(BaseUiState(isShowShimmer = false, data = 0)) }
            }.stateInViewModel(BaseUiState(isShowShimmer = true, data = 0)),

        currentWalkEggUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshHome }
            .flatMapLatest {
                getCurrentWalkEgg
                    .invoke(Unit)
                    .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                    .catch {
                        emit(
                            BaseUiState(
                                isShowShimmer = false,
                                data = MyEggModel.empty()
                            )
                        )
                    }
            }.stateInViewModel(
                BaseUiState(
                    isShowShimmer = true,
                    data = MyEggModel.empty()
                )
            ),
        currentWalkCharacterUiState = viewModelEvent.filter { it == HomeScreenViewModelEvent.RefreshHome }
            .flatMapLatest {
                getCurrentWalkCharacter.invoke(Unit)
                    .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                    .catch {
                        emit(
                            BaseUiState(
                                isShowShimmer = false,
                                data = WalkieCharacter.ofEmpty()
                            )
                        )
                    }
            }.stateInViewModel(BaseUiState(isShowShimmer = true, data = WalkieCharacter.ofEmpty())),
        showActivityRecognitionPermissionAlert = _showActivityPermissionAlert.stateInViewModel(false),
        showBackgroundLocationPermissionAlert = _showBackgroundLocationPermissionAlert.stateInViewModel(
            false
        ),
        userInfo = getMyData.invoke(Unit).catch { }.stateInViewModel(null)

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
            EventContainer.hatchingAnimationFlow.collect { isHatching ->
                if (isHatching) {
                    getCurrentWalkEgg.invoke(Unit)
                        .map { BaseUiState(isShowShimmer = false, data = it.toUiModel()) }
                        .catch { emit(BaseUiState(isShowShimmer = false, data = MyEggModel.empty())) }
                        .collect { eggState ->
                            updateHatchingInfo(eggState.data)

                            if (eggState.data.eggId != 0L) {
                                updateEggWithLocationData(eggState.data.eggId, dataStore.getEggCurrentSteps())
                            }
                        }
                } else {
                    _hatchingInfo.value = BaseUiState(
                        isShowShimmer = false,
                        data = HatchingAnimationCharacterData(isHatching = false)
                    )
                }
            }
        }
    }

    private fun updateHatchingInfo(eggModel: MyEggModel) {
        _hatchingInfo.value = BaseUiState(
            isShowShimmer = false,
            data = HatchingAnimationCharacterData(
                isHatching = true,
                character = eggModel.walkieCharacter,
                eggKind = eggModel.eggKind
            )
        )
    }

    private fun processUserInfo() {
        viewModelScope.launch {
            _state.userInfo
                .collect { userInfo ->
                    if (userInfo != null) {
                        Printer.e("JUNWOO", "eggId : ${userInfo.eggId}")
                        updateStepProgress(userInfo)
                    }
                }
        }
    }

    private fun updateStepProgress(userInfo: UserInfo) {
        viewModelScope.launch {
            if (userInfo.eggId == 0L || dataStore.getHatchingTargetStep() == 0) return@launch
            val remainingStep = dataStore.getHatchingTargetStep() - dataStore.getEggCurrentSteps()

            if (remainingStep > 0) {
                // 남은 걸음수가 있는 경우
                Printer.e("JUNWOO", "remainingStep : $remainingStep")
                updateStepWithStepCount(userInfo.eggId, dataStore.getEggCurrentSteps())
            } else {
                // 목표 달성한 경우 - 부화 애니메이션 동작
                viewModelScope.launch {
                    _state.currentWalkEggUiState
                        .filter { !it.isShowShimmer && it.data.eggKind != EggKind.Empty }
                        .take(1)
                        .collect {
                            EventContainer.triggerHatchingAnimation()
                        }
                }
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
                ).catch {}.onCompletion {
                    stepCounterService.resetEggStep()
                    EventContainer.triggerNotificationUpdate(0)
                    notifyViewModelEvent(HomeScreenViewModelEvent.RefreshHome)
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun setupTargetSteps() {
        viewModelScope.launch {
            _state.currentWalkEggUiState
                .filter { !it.isShowShimmer }
                .take(1)
                .collect { currentEgg ->
                    val targetStep = if (currentEgg.data.eggKind == EggKind.Empty) {
                        0
                    } else {
                        currentEgg.data.needStep // 알이 있으면 실제 목표값
                    }
                    dataStore.setHatchingTargetStep(target = targetStep)
                }
        }
    }

    fun startCounting() {
        stepCounterService.startCounting()
    }

    fun stopCounting() {
        stepCounterService.stopCounting()
    }

    fun resetTodayStepCount() {
        viewModelScope.launch(Dispatchers.IO) {
            stepCounterService.checkAndResetForNewDay()
        }
    }


    fun setActivityPermissionAlertState(show: Boolean) {
        _showActivityPermissionAlert.value = show
    }

    fun setBackgroundLocationPermissionAlertState(show: Boolean) {
        _showBackgroundLocationPermissionAlert.value = show
    }

    fun onEventModalDismissed() {
        _eventState.value = _eventState.value.copy(showEventModal = false)
    }

    fun navigateToGainEggFromEventModal() {
        _eventState.value = _eventState.value.copy(showEventModal = false)
        viewModelScope.launch {
            _navigateToGainEgg.emit(Unit)
        }
    }

    fun callDailyApiAndCheckEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val shouldCallEventApi = runCatching {
                userRepository.isEggEventEnabled()
            }.getOrElse { exception ->
                Printer.e("JUNWOO", "RemoteConfig call failed: $exception")
                false
            }

            if (shouldCallEventApi && dataStore.shouldCallDailyApi()) {
                getDailyEgg(Unit)
                    .catch { throwable ->
                        Printer.e("JUNWOO", "Daily egg API call failed: ${throwable.message}")
                    }
                    .collect { dailyEgg ->
                        Printer.d(
                            "JUNWOO",
                            "Daily egg API response: receivedToday=${dailyEgg.receivedToday}, remainingDays=${dailyEgg.remainingDays}"
                        )
                        _eventState.value = _eventState.value.copy(
                            eventRemainingDays = dailyEgg.remainingDays,
                            showEventModal = dailyEgg.receivedToday // true 일 경우 모달 노출
                        )
                    }

                // API 호출 완료 후 마킹
                dataStore.markDailyApiCalled()
            } else {
                if (!shouldCallEventApi) {
                    Printer.d("JUNWOO", "Event API disabled by remote config")
                } else {
                    Printer.d("JUNWOO", "Daily API already called today")
                }
            }
        }
    }
}

interface HomeViewState : BaseState {
    val stepsUiState: StateFlow<BaseUiState<Pair<Int, Int>>>
    val currentHatchedCharacterCountUiState: StateFlow<BaseUiState<Int>>
    val currentGainEggCountUiState: StateFlow<BaseUiState<Int>>
    val currentRecordedSpotCountUiState: StateFlow<BaseUiState<Int>>
    val currentWalkEggUiState: StateFlow<BaseUiState<MyEggModel>>
    val currentWalkCharacterUiState: StateFlow<BaseUiState<WalkieCharacter>>
    val showActivityRecognitionPermissionAlert: StateFlow<Boolean>
    val showBackgroundLocationPermissionAlert: StateFlow<Boolean>
    val userInfo: StateFlow<UserInfo?>
}

class HomeViewStateImpl(
    override val stepsUiState: StateFlow<BaseUiState<Pair<Int, Int>>>,
    override val currentHatchedCharacterCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentGainEggCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentRecordedSpotCountUiState: StateFlow<BaseUiState<Int>>,
    override val currentWalkEggUiState: StateFlow<BaseUiState<MyEggModel>>,
    override val currentWalkCharacterUiState: StateFlow<BaseUiState<WalkieCharacter>>,
    override val showActivityRecognitionPermissionAlert: StateFlow<Boolean>,
    override val showBackgroundLocationPermissionAlert: StateFlow<Boolean>,
    override val userInfo: StateFlow<UserInfo?>
) : HomeViewState

data class EventUiState(
    val showEventModal: Boolean = false,
    val eventRemainingDays: Int = 0
)

data class HatchingAnimationCharacterData(
    val isHatching: Boolean = false,
    val character: WalkieCharacter = WalkieCharacter.ofEmpty(),
    val eggKind: EggKind = EggKind.Empty
)