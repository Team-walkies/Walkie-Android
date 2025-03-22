package com.startup.common.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EggHatchingEvent {
    private val _hatchingAnimationFlow = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val hatchingAnimationFlow: SharedFlow<Boolean> = _hatchingAnimationFlow.asSharedFlow()

    suspend fun triggerHatchingAnimation() {
        _hatchingAnimationFlow.emit(true)
    }
}