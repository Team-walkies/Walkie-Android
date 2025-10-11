package com.startup.home.healthcare

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseUiState
import com.startup.common.base.BaseViewModel
import com.startup.common.util.DateUtil
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.common.util.Printer
import com.startup.common.util.ResponseErrorException
import com.startup.domain.model.egg.GetEggAWard
import com.startup.domain.repository.LocationRepository
import com.startup.domain.usecase.healthcare.GetCalendarHealthcareContinueDays
import com.startup.domain.usecase.healthcare.GetDailyHealthcareDetail
import com.startup.domain.usecase.healthcare.GetEggAwardSpecificDate
import com.startup.domain.usecase.healthcare.GetRangeOfWeekDailyHealthcareList
import com.startup.domain.usecase.healthcare.GetTargetStep
import com.startup.domain.usecase.healthcare.PutTargetStep
import com.startup.model.healthcare.DailyHealthcareDetailModel
import com.startup.model.healthcare.DailyHealthcareDetailModel.Companion.toUiModel
import com.startup.model.healthcare.DailyHealthcareListItemModel.Companion.toUiModel
import com.startup.model.spot.CalendarModel
import com.startup.model.spot.WeekFetchDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HealthcareViewModel @Inject constructor(
    private val getDailyHealthcareDetail: GetDailyHealthcareDetail,
    private val getRangeOfWeekDailyHealthcareList: GetRangeOfWeekDailyHealthcareList,
    private val putTargetStep: PutTargetStep,
    private val getEggAwardSpecificDate: GetEggAwardSpecificDate,
    private val locationRepository: LocationRepository,
    @ApplicationContext private val context: Context,
    getCalendarHealthcareContinueDays: GetCalendarHealthcareContinueDays,
    getTargetStep: GetTargetStep,
) : BaseViewModel() {
    private val _state = HealthcareViewStateImpl(
        currentContinueDays = getCalendarHealthcareContinueDays.invoke(Unit).catch { emit(0) }.stateInViewModel(0),
        todayTargetStep = getTargetStep.invoke(Unit).catch { emit(0) }.stateInViewModel(0)
    )
    override val state: HealthcareViewState get() = _state
    private var detailSearchJob: Job? = null

    init {
        initializeHealthcare()
    }

    override fun handleViewModelEvent(event: BaseEvent) {
        when (event) {
            is HealthcareUiEvent.OnDateChanged -> {
                changedSelectedDate(event.calendarModel)
            }

            is HealthcareUiEvent.OnTargetStepChanged -> {
                onTargetStepChange(event.targetStep)
            }

            is HealthcareUiEvent.GetEgg -> {
                getEggSelectedDate(event.targetDate)
            }
        }
    }

    private fun onTargetStepChange(targetStep: Int) {
        viewModelScope.launch {
            putTargetStep.invoke(targetStep)
        }
    }

    private fun initializeHealthcare() {
        val currentDate = _state.currentSelectedDate.value.date
        val weeksToFetch = getWeekRangeList(currentDate, WeekFetchDirection.ALL_THREE)
        fetchCurrentDateDetail(currentDate)
        fetchWeeklyHealthcareList(
            date = _state.currentSelectedDate.value.date,
            weeksToFetch = weeksToFetch
        )
    }

    private fun changedSelectedDate(date: CalendarModel) {
        val moveDirection = checkWeekFetchDirection(
            date = date.date,
            previousDate = _state.currentSelectedDate.value.date
        )
        val weeksToFetch = getWeekRangeList(
            currentFocusDate = date.date,
            moveDirection = moveDirection
        )
        fetchCurrentDateDetail(date.date)
        fetchWeeklyHealthcareList(date.date, weeksToFetch)
        _state.currentSelectedDate.update { date }
        Printer.e("LMH", "WEEK CHANGED $date")
    }

    private fun fetchCurrentDateDetail(date: LocalDate) {
        detailSearchJob?.cancel()
        detailSearchJob = viewModelScope.launch {
            getDailyHealthcareDetail.invoke(date)
                .onStart {
                    _state.currentDailyHealthcareDetail.update {
                        BaseUiState(
                            isShowShimmer = true,
                            DailyHealthcareDetailModel.orEmpty()
                        )
                    }
                }
                .map {
                    it.toUiModel()
                }.catch {
                    Printer.e("LMH", "getDetailError")
                    if (it !is CancellationException) {
                        _state.currentDailyHealthcareDetail.update {
                            BaseUiState(
                                isShowShimmer = false,
                                DailyHealthcareDetailModel.orEmpty()
                            )
                        }
                    }
                }.collect { item ->
                    Printer.d("LMH", "get Detail $item")
                    _state.currentDailyHealthcareDetail.update { BaseUiState(isShowShimmer = false, item) }
                }
        }
    }

    private fun fetchWeeklyHealthcareList(
        date: LocalDate,
        weeksToFetch: List<Pair<LocalDate, LocalDate>>
    ) {
        // Current 의 경우 emptyList 가 넘어옴
        if (weeksToFetch.isEmpty()) return

        val newWeekStart = getStartOfWeek(date)
        // 현재 주 기준으로 이전주, 다음주까지만 홀딩하기 위함
        val weeksToLoad = listOf(
            newWeekStart.minusWeeks(1).toString(),
            newWeekStart.toString(),
            newWeekStart.plusWeeks(1).toString()
        )

        Printer.d("LMH", "WEEK LIST $weeksToFetch")

        // 해당 range 리스트에서 가장 빠른 일자(startDate 로 들어가기 위함)
        val startDate = weeksToFetch.minOf { it.first }
        val startDateStr = DateUtil.convertTranslatorDateFormat(startDate)
        // 해당 range 리스트에서 가장 미래의 일자(endDate 로 들어가기 위함)
        val endDate = weeksToFetch.maxOf { it.second }.let { maxDate ->
            if (maxDate.isAfter(LocalDate.now())) LocalDate.now() else maxDate
        }
        val endDateStr = DateUtil.convertTranslatorDateFormat(endDate)

        Printer.d("LMH", "Request Calendar List $startDateStr ~ $endDateStr")

        viewModelScope.launch {
            getRangeOfWeekDailyHealthcareList
                .invoke(startDateStr to endDateStr)
                .onStart {
                    _state.healthCareList.value = _state.healthCareList.value
                        .filterKeys { it in weeksToLoad }
                        .toMutableMap()
                        .apply {
                            putAll(weeksToFetch.map {
                                getStartOfWeek(it.first).toString() to emptyList()
                            })
                        }
                }
                .map {
                    it.map { domainModel -> domainModel.toUiModel() }
                }
                .onEach { fullEventList ->
                    Printer.d("LMH", "EVENT!! $fullEventList")

                    // key 는 주의 첫날을 key 로 하기 위해 getStartOfWeek 사용
                    val grouped = fullEventList.groupBy { event ->
                        getStartOfWeek(event.date).toString()
                    }
                    Printer.d(
                        tag = "LMH",
                        message = "before filtering event Map ${_state.healthCareList.value.entries.toList()}"
                    )
                    // 새로운 이벤트로 replace 시킴, 다만 이전주, 현재주, 다음주까지만 들고 있게함
                    _state.healthCareList.value = _state.healthCareList.value
                        .filterKeys { it in weeksToLoad }
                        .toMutableMap()
                        .apply {
                            putAll(grouped.map { it.key to it.value } +
                                    filter { !grouped.keys.contains(it.key) }
                                        .map { it.key to it.value }
                            )
                        }
                    Printer.d(
                        "LMH",
                        "after filtering event Map ${_state.healthCareList.value.entries.toList()}"
                    )
                }
                .catch { }
                .launchIn(viewModelScope)
        }
    }


    private fun checkWeekFetchDirection(
        date: LocalDate,
        previousDate: LocalDate
    ): WeekFetchDirection {
        val previousWeekStart = getStartOfWeek(previousDate)
        val newWeekStart = getStartOfWeek(date)
        val weekDiff =
            kotlin.runCatching { ChronoUnit.WEEKS.between(previousWeekStart, newWeekStart) }
                .getOrElse { 0L }

        return when (weekDiff) {
            -1L -> WeekFetchDirection.PREVIOUS_WEEK
            0L -> WeekFetchDirection.CURRENT_WEEK
            1L -> WeekFetchDirection.NEXT_WEEK
            else -> WeekFetchDirection.ALL_THREE
        }
    }

    private fun getWeekRangeList(
        currentFocusDate: LocalDate,
        moveDirection: WeekFetchDirection
    ): List<Pair<LocalDate, LocalDate>> {
        val newWeekStart = getStartOfWeek(currentFocusDate)

        Printer.d("LMH", "주간 요청 타입 $moveDirection")

        return when (moveDirection) {
            WeekFetchDirection.CURRENT_WEEK -> emptyList() // 같은 주면 요청 안 함

            // 바뀔 주 기준으로 이전주 까지의 데이터만 받아옴
            WeekFetchDirection.PREVIOUS_WEEK -> {
                val start = newWeekStart.minusWeeks(1)
                val end = start.plusDays(6)
                listOf(start to end)
            }

            // 바뀔 주 기준으로 다음주 까지의 데이터만 받아옴
            WeekFetchDirection.NEXT_WEEK -> {
                val start = newWeekStart.plusWeeks(1)
                val end = start.plusDays(6)
                listOf(start to end)
            }

            // 이전주 현재주 다음 주 데이터를 받아오기 위함
            WeekFetchDirection.ALL_THREE -> {
                val prev = newWeekStart.minusWeeks(1) to newWeekStart.minusWeeks(1).plusDays(6)
                val curr = newWeekStart to newWeekStart.plusDays(6)
                val next = newWeekStart.plusWeeks(1) to newWeekStart.plusWeeks(1).plusDays(6)
                listOf(prev, curr, next)
            }
        }
    }

    private fun getEggSelectedDate(targetDate: LocalDate) {
        viewModelScope.launch {

            val location = locationRepository.getCurrentLocation().firstOrNull()
            getEggAwardSpecificDate
                .invoke(
                    GetEggAWard(
                        longitude = location?.longitude ?: -1.0,
                        latitude = location?.latitude ?: -1.0,
                        healthcareEggAcquiredAt = DateUtil.convertTranslatorDateFormat(targetDate)
                    )
                )
                .catch {
                    if (it is ResponseErrorException) {

                    }
                }
                .collect {
                    initializeHealthcare()
                }
        }
    }
}