package com.startup.home.spot

import com.startup.common.base.BaseViewModel
import com.startup.home.spot.model.CalendarModel
import com.startup.home.spot.model.SpotArchiveViewState
import com.startup.home.spot.model.SpotArchiveViewStateImpl
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SpotArchiveViewModel @Inject constructor(): BaseViewModel() {
    private val _state = SpotArchiveViewStateImpl()
    override val state: SpotArchiveViewState = _state

    fun changedSelectedDate(date: CalendarModel) {
        _state.currentSelectedDate.update { date }
    }
}