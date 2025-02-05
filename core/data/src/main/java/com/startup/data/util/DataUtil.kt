package com.startup.data.util

import android.util.Log
import com.google.gson.JsonParseException
import retrofit2.HttpException
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLPeerUnverifiedException

sealed interface WalkieException

class ClientException(
    override val message: String? = null,
    val code: Int? = null,
    val body: Response<*>? = null
) : Exception(message), WalkieException

class UnknownException(
    override val message: String? = null,
    val code: Int? = null
) : Exception(message), WalkieException

class NoCharacterException(
    override val message: String? = null,
    val code: Int? = null
) : Exception(message), WalkieException

class NetworkTemporaryException(
    override val message: String? = null,
    val code: Int? = null
) : Exception(message), WalkieException

fun handleException(e: Throwable): Throwable {
    when (e) {
        is JsonParseException -> {
            throw ClientException(message = e.message)
        }
        is HttpException -> when (e.code()) {
            in 400..499 -> throw ClientException(
                message = e.message,
                code = e.code(),
                body = e.response()
            )   //401 code is may processing interceptor
            500, 502, 503, 504 -> {
                throw NetworkTemporaryException(message = e.message(), code = e.code())
            }
            else -> throw e
        }
        is NoCharacterException -> throw e
        is SocketTimeoutException,
        is ConnectException,
        is UnknownHostException,
        is SSLHandshakeException,
        is SSLPeerUnverifiedException,
        is SSLException,
        is InterruptedIOException -> throw NetworkTemporaryException(message = e.message)
        else -> throw UnknownException(message = e.message)
    }
}

suspend fun <T, R> R.handleExceptionIfNeed(function: suspend R.() -> T): T {
    return try {
        function(this)
    } catch (e: Exception) {
        Log.e("checkException", "handleException $e")
        throw handleException(e)
    }
}