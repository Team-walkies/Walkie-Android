package com.startup.home.spot

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.base.BaseUiState
import com.startup.common.util.DateUtil
import com.startup.common.util.DateUtil.getStartOfWeek
import com.startup.common.util.Printer
import com.startup.domain.usecase.review.DeleteReview
import com.startup.domain.usecase.review.GetRangeOfWeekReviewList
import com.startup.home.spot.model.CalendarModel
import com.startup.home.spot.model.ReviewModel
import com.startup.home.spot.model.ReviewModel.Companion.toUiModel
import com.startup.home.spot.model.SpotArchiveViewState
import com.startup.home.spot.model.SpotArchiveViewStateImpl
import com.startup.home.spot.model.WeekFetchDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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
class SpotArchiveViewModel @Inject constructor(
    private val getRangeOfWeekReviewList: GetRangeOfWeekReviewList,
    private val deleteReview: DeleteReview
) : BaseViewModel() {
    private val _state = SpotArchiveViewStateImpl()
    override val state: SpotArchiveViewState = _state

    init {
        initializeReviewList()
    }

    fun initializeReviewList() {
        val currentDate = _state.currentSelectedDate.value.date
        val weeksToFetch = getWeekRangeList(currentDate, WeekFetchDirection.ALL_THREE)
        fetchWeeklyReviewList(
            date = _state.currentSelectedDate.value.date,
            weeksToFetch = weeksToFetch
        )
    }

    fun deleteReview(review: ReviewModel) {
        Printer.d("LMH", "deleteReview $review")
        deleteReview
            .invoke(review.reviewId)
            .onEach {
                initializeReviewList()
            }
            .catch { }
            .launchIn(viewModelScope)
    }

    fun changedSelectedDate(date: CalendarModel) {
        val moveDirection = checkWeekFetchDirection(
            date = date.date,
            previousDate = _state.currentSelectedDate.value.date
        )
        val weeksToFetch = getWeekRangeList(
            currentFocusDate = date.date,
            moveDirection = moveDirection
        )
        fetchWeeklyReviewList(date.date, weeksToFetch)
        _state.currentSelectedDate.update { date }
        Printer.e("LMH", "WEEK CHANGED $date")
    }

    private fun fetchWeeklyReviewList(
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
            getRangeOfWeekReviewList
                .invoke(startDateStr to endDateStr)
                .onStart {
                    _state.eventList.value = _state.eventList.value
                        .filterKeys { it in weeksToLoad }
                        .toMutableMap()
                        .apply {
                            putAll(weeksToFetch.map {
                                getStartOfWeek(it.first).toString() to BaseUiState(
                                    isShowShimmer = true,
                                    emptyList()
                                )
                            })
                        }
                }
                .map { it.map { domainModel -> domainModel.toUiModel() } }
                .onEach { fullEventList ->
                    Printer.d("LMH", "EVENT!! $fullEventList")

                    // key 는 주의 첫날을 key 로 하기 위해 getStartOfWeek 사용
                    val grouped = fullEventList.groupBy { event ->
                        getStartOfWeek(event.date).toString()
                    }
                    Printer.d(
                        tag = "LMH",
                        message = "before filtering event Map ${_state.eventList.value.entries.toList()}"
                    )
                    // 새로운 이벤트로 replace 시킴, 다만 이전주, 현재주, 다음주까지만 들고 있게함
                    _state.eventList.value = _state.eventList.value
                        .filterKeys { it in weeksToLoad }
                        .toMutableMap()
                        .apply {
                            putAll(grouped.map {
                                it.key to BaseUiState(isShowShimmer = false, it.value)
                            } + filter { !grouped.keys.contains(it.key) }
                                .map { it.key to BaseUiState(isShowShimmer = false, it.value.data) }
                            )
                        }
                    Printer.d(
                        "LMH",
                        "after filtering event Map ${_state.eventList.value.entries.toList()}"
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
}