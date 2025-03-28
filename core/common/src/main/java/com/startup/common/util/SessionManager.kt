package com.startup.common.util

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _sessionExpiredFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val sessionExpiredFlow: SharedFlow<Unit> = _sessionExpiredFlow

    fun notifySessionExpired() {
        _sessionExpiredFlow.tryEmit(Unit)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    fun clearSessionState() {
        _sessionExpiredFlow.resetReplayCache()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface SessionManagerEntryPoint {
    fun sessionManager(): SessionManager
}