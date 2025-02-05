package com.startup.domain.util

import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<R, P> {
    abstract operator fun invoke(params: P): Flow<R>
}