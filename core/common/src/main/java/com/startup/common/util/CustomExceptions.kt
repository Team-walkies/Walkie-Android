package com.startup.common.util

sealed interface WalkieException

class ClientException(
    override val message: String? = null,
    val code: Int? = null,
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

class ResponseErrorException(
    override val message: String,
    val code: Int? = null
) : Exception(message), WalkieException

class UserAuthNotFoundException(
    override val message: String? = null,
    val providerToken: String,
    val code: Int? = null
) : Exception(message), WalkieException

class KakaoAuthFailException(
    override val message: String? = null,
    val code: Int? = null
) : Exception(message), WalkieException

class SessionExpireException(
    override val message: String? = null,
    val code: Int? = null
) : Exception(message), WalkieException
