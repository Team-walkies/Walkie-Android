package com.startup.home.step

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.base.State
import com.startup.stepcounter.service.StepCounterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepCounterViewModel @Inject constructor(
    private val stepCounter: StepCounterService,
) : BaseViewModel() {
    private val _state = mutableStateOf(StepCounterState())
    override val state: State
        get() = _state.value

    init {
        viewModelScope.launch {
            stepCounter.observeSteps()
                .collect { steps ->
                    _state.value = StepCounterState(steps = steps)
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
}

data class StepCounterState(
    val steps: Int = 0
) : State