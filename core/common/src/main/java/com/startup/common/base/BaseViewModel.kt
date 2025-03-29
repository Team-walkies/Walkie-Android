package com.startup.common.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    abstract val state: BaseState

    private val _event = Channel<BaseEvent>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val event = _event.receiveAsFlow()

    val viewModelEvent = MutableSharedFlow<BaseEvent>(
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val ioDispatcher = Dispatchers.IO

    init {
        viewModelScope.launch {
            viewModelEvent.collect(::handleViewModelEvent)
        }
    }

    fun notifyViewModelEvent(event: BaseEvent) {
        viewModelScope.launch {
            viewModelEvent.emit(event)
        }
    }

    open fun handleViewModelEvent(event: BaseEvent) {}

    protected fun notifyEvent(event: BaseEvent) {
        coroutineScope.launch {
            _event.send(event)
        }
    }

    protected fun <R, P, MR> BaseUseCase<R, P>.executeOnViewModel(
        params: P = Unit as P,
        onMap: (R) -> MR,
        onEach: (MR) -> Unit,
        onError: (Throwable) -> Unit,
        onCanceled: () -> Unit = {},
        onCompleted: () -> Unit = {},
    ): Flow<MR> {
        return invoke(params)
            .map(onMap)
            .onEach(onEach)
            .catch {
                onError.invoke(it)
            }.onCompletion {
                when (it) {
                    is CancellationException -> onCanceled()
                }
                onCompleted()
            }.flowOn(ioDispatcher)
    }

    protected fun <R, P> BaseUseCase<R, P>.executeOnViewModel(
        params: P = Unit as P,
        onEach: (R) -> Unit,
        onError: (Throwable) -> Unit,
        onCanceled: () -> Unit = {},
        onCompleted: () -> Unit = {},
    ): Flow<R> {
        return invoke(params)
            .onEach(onEach)
            .catch {
                onError.invoke(it)
            }.onCompletion {
                when (it) {
                    is CancellationException -> onCanceled()
                }
                onCompleted()
            }.flowOn(ioDispatcher)
    }

    protected fun <T> Flow<T>.stateInViewModel(
        initialValue: T,
        scope: CoroutineScope = viewModelScope
    ): StateFlow<T> = this.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue
    )
}