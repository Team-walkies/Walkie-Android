package com.startup.data.remote.ext

import com.startup.common.util.ResponseErrorException
import com.startup.data.remote.BaseResponse
import kotlinx.coroutines.flow.FlowCollector

internal suspend fun <T : BaseResponse<R>, R> FlowCollector<R>.emitRemote(
    item: T,
    specificErrorCode: Int? = null
) {
    if (specificErrorCode != null && item.status == specificErrorCode) {
        throw ResponseErrorException(item.message.orEmpty(), item.status)
    }
    if (item.status !in 200..299) {
        throw ResponseErrorException(item.message.orEmpty(), (item.status ?: -1))
    }
    emit(item.data.requireNotNull())
}


internal fun <T> T?.requireNotNull(): T {
    return when (this) {
        is Unit? -> Unit as T // Unit? 타입이면 Unit 반환
        is List<*> -> emptyList<Any?>() as T
        else -> this ?: throw NullPointerException("값이 없음")
    }
}