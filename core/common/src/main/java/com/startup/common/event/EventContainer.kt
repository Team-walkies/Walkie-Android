package com.startup.common.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventContainer {
    private val _hatchingAnimationFlow = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val hatchingAnimationFlow: SharedFlow<Boolean> = _hatchingAnimationFlow.asSharedFlow()

    private val _updateNotificationFlow = MutableSharedFlow<Int>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val updateNotificationFlow = _updateNotificationFlow.asSharedFlow()

    private val _homeRefreshEventFlow = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val homeRefreshEventFlow: SharedFlow<Unit> = _homeRefreshEventFlow.asSharedFlow()

    suspend fun triggerHatchingAnimation() {
        _hatchingAnimationFlow.emit(true)
    }

    suspend fun onRefreshEvent() {
        _homeRefreshEventFlow.emit(Unit)
    }

    suspend fun triggerNotificationUpdate(targetStep: Int) {
        _updateNotificationFlow.emit(targetStep)
    }

}