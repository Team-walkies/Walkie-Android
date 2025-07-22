package com.startup.home.healthcare

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseUiState
import com.startup.model.healthcare.DailyHealthcareDetailModel
import com.startup.model.healthcare.DailyHealthcareListItemModel
import com.startup.model.spot.CalendarModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface HealthcareViewState : BaseState {
    val currentSelectedDate: StateFlow<CalendarModel>
    val currentDailyHealthcareDetail: StateFlow<BaseUiState<DailyHealthcareDetailModel>>
    val healthCareList: StateFlow<Map<String, List<DailyHealthcareListItemModel>>>
    val currentContinueDays: StateFlow<Int>
}

class HealthcareViewStateImpl(override val currentContinueDays: StateFlow<Int>) : HealthcareViewState {
    override val currentSelectedDate: MutableStateFlow<CalendarModel> =
        MutableStateFlow(CalendarModel(LocalDate.now(), isSpecificDate = true))
    // TODO isShowShimmer true 로 다시 바꿔야함
    override val currentDailyHealthcareDetail: MutableStateFlow<BaseUiState<DailyHealthcareDetailModel>> =
        MutableStateFlow(BaseUiState(isShowShimmer = false, data = DailyHealthcareDetailModel.orEmpty()))
    override val healthCareList: MutableStateFlow<Map<String, List<DailyHealthcareListItemModel>>> =
        MutableStateFlow(mapOf())
}

sealed interface HealthcareUiEvent : BaseEvent {
    data class OnDateChanged(val calendarModel: CalendarModel) : HealthcareUiEvent
    data class OnGoalChanged(val targetStep: Int) : HealthcareUiEvent
}
