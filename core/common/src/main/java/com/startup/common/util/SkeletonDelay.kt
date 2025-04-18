package com.startup.common.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.skeletonWithDelay(defaultValue: T, delayMillis: Long = 5000): Flow<BaseUiState<T>> =
    this.map { BaseUiState(isShowShimmer = false, data = it) }
        .onStart {
            emit(BaseUiState(isShowShimmer = true, data = defaultValue))
            delay(delayMillis)
        }
        .catch { emit(BaseUiState(isShowShimmer = false, data = defaultValue)) }