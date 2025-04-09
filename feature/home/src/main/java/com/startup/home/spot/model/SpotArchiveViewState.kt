package com.startup.home.spot.model

import com.startup.common.base.BaseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
}