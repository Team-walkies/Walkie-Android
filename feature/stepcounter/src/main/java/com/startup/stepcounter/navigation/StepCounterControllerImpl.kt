package com.startup.stepcounter.navigation

import com.startup.domain.provider.StepCounterService
import com.startup.navigation.StepCounterController
import javax.inject.Inject

class StepCounterControllerImpl @Inject constructor(private val stepCounterService: StepCounterService) :
    StepCounterController {
    override fun startWalkieStepForegroundService() {
        stepCounterService.startCounting()
    }

    override fun stopWalkieStepForegroundService() {
        stepCounterService.stopCounting()
    }
}