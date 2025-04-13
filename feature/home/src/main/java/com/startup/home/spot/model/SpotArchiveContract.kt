package com.startup.home.spot.model

import com.startup.common.base.BaseState
import com.startup.common.base.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

interface SpotArchiveViewState : BaseState {
    val currentSelectedDate: StateFlow<CalendarModel>
    val eventList: StateFlow<Map<String, List<ReviewModel>>>
}

class SpotArchiveViewStateImpl : SpotArchiveViewState {
    override val currentSelectedDate: MutableStateFlow<CalendarModel> =
        MutableStateFlow(CalendarModel(LocalDate.now(), isSpecificDate = true))
    override val eventList: MutableStateFlow<Map<String, List<ReviewModel>>> =
        MutableStateFlow(mapOf())

    fun clearEventList() {
        eventList.update { mapOf() }
    }
}

sealed interface SpotArchiveUiEvent : UiEvent {
    data object OnBack : SpotArchiveUiEvent
    data class OnDateChanged(val calendarModel: CalendarModel) : SpotArchiveUiEvent
    data class OnDeleteReview(val review: ReviewModel) : SpotArchiveUiEvent
    data class OnModifyReview(val review: ReviewModel) : SpotArchiveUiEvent
}