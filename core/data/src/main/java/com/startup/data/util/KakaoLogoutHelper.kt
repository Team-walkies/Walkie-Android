package com.startup.data.util

import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class KakaoLogoutHelper @Inject constructor() {
    /**
     * 로그아웃
     */
    suspend fun logout(): Result<Unit> = runCatching {
        UserApiClient.logOut()
    }

    private suspend fun UserApiClient.Companion.unLink() {
        suspendCoroutine { continuation ->
            instance.unlink { error ->
                continuation.resumeDisConnect(error)
            }
        }
    }

    /**
     * 연결끊기 회원탈퇴
     */
    suspend fun unLink(): Result<Unit> = runCatching {
        UserApiClient.unLink()
    }
    /**
     * 카카오 사용자 정보 요청
     */
    suspend fun me(): Result<User> = runCatching {
        UserApiClient.me()
    }

    private suspend fun UserApiClient.Companion.me(): User = suspendCoroutine { continuation ->
        instance.me { user, error ->
            continuation.resumeUserInfo(user, error)
        }
    }

    private suspend fun UserApiClient.Companion.logOut() {
        suspendCoroutine { continuation ->
            instance.logout { error ->
                continuation.resumeDisConnect(error)
            }
        }
    }

    private fun Continuation<Unit>.resumeDisConnect(
        error: Throwable?
    ) {
        if (error != null) {
            resumeWithException(error)
        } else {
            resume(Unit)
        }
    }

    private fun Continuation<User>.resumeUserInfo(
        user: User?, error: Throwable?
    ) {
        if (error != null) {
            resumeWithException(error)
        } else if (user != null) {
            resume(user)
        } else {
            resumeWithException(RuntimeException("사용자 정보 접근 에러"))
        }
    }

}