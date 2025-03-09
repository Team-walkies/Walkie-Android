package com.startup.walkie.navigationimpl

import com.startup.navigation.StepCounterController
import com.startup.stepcounter.service.StepCounterService
import javax.inject.Inject

class StepCounterControllerImpl @Inject constructor(private val stepCounterService: StepCounterService): StepCounterController {
    override fun startWalkieStepForegroundService() {
        stepCounterService.startCounting()
    }

    override fun stopWalkieStepForegroundService() {
        stepCounterService.stopCounting()
    }
}