package com.startup.common.event

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow

object EventContainer {
    private val _hatchingAnimationChannel = Channel<Unit>(capacity = Channel.BUFFERED)

    val hatchingAnimationFlow: Flow<Unit> = _hatchingAnimationChannel.receiveAsFlow()

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
        _hatchingAnimationChannel.send(Unit)
    }

    suspend fun onRefreshEvent() {
        _homeRefreshEventFlow.emit(Unit)
    }

    suspend fun triggerNotificationUpdate(targetStep: Int) {
        _updateNotificationFlow.emit(targetStep)
    }

}