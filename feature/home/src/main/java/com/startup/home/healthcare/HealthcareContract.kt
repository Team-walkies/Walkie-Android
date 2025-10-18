package com.startup.home.healthcare

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseUiState
import com.startup.model.egg.EggDetailModel
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
    val todayTargetStep: StateFlow<Int>
}

class HealthcareViewStateImpl(
    override val currentContinueDays: StateFlow<Int>,
    override val todayTargetStep: StateFlow<Int>
) : HealthcareViewState {
    override val currentSelectedDate: MutableStateFlow<CalendarModel> =
        MutableStateFlow(CalendarModel(LocalDate.now(), isSpecificDate = true))

    override val currentDailyHealthcareDetail: MutableStateFlow<BaseUiState<DailyHealthcareDetailModel>> =
        MutableStateFlow(BaseUiState(isShowShimmer = true, data = DailyHealthcareDetailModel.orEmpty()))
    override val healthCareList: MutableStateFlow<Map<String, List<DailyHealthcareListItemModel>>> =
        MutableStateFlow(mapOf())
}

sealed interface HealthcareUiEvent : BaseEvent {
    data class OnDateChanged(val calendarModel: CalendarModel) : HealthcareUiEvent
    data class OnTargetStepChanged(val targetStep: Int) : HealthcareUiEvent
    data class GetEgg(val targetDate: LocalDate) : HealthcareUiEvent
}

sealed interface HealthcareViewModelEvent : BaseEvent {
    data class GetAwardOfEgg(val eggDetail: EggDetailModel) : HealthcareViewModelEvent
}