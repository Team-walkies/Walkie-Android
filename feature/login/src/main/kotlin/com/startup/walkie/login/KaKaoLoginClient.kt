package com.startup.walkie.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KaKaoLoginClient(
    private val context: Context,
    private val userApiClient: UserApiClient = UserApiClient.instance
) {
    /**
     * 앱/웹 통합 카카오로그인 함수
     */
    suspend fun login(): Result<OAuthToken> = runCatching {
        if (userApiClient.isKakaoTalkLoginAvailable(context)) { // 카카오톡이 설치되어있는가?
            try {
                UserApiClient.loginWithKakaoTalk(context)
            } catch (error: Throwable) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    // 사용자가 뒤로가기 선택으로 의도적 취소
                    throw error
                } else {
                    UserApiClient.loginWithKakaoAccount(context)
                }
            }
        } else { // 카카오계정(웹)으로 로그인
            UserApiClient.loginWithKakaoAccount(context)
        }
    }

    /**
     * 카카오톡(앱)으로 로그인 시도
     */
    private suspend fun UserApiClient.Companion.loginWithKakaoTalk(context: Context): OAuthToken =
        suspendCoroutine { continuation ->
            instance.loginWithKakaoTalk(context) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }

    /**
     * 카카오계정(웹)으로 로그인 시도
     */
    private suspend fun UserApiClient.Companion.loginWithKakaoAccount(context: Context): OAuthToken =
        suspendCoroutine { continuation ->
            instance.loginWithKakaoAccount(context) { token, error ->
                continuation.resumeTokenOrException(token, error)
            }
        }

    private suspend fun UserApiClient.Companion.me(): User = suspendCoroutine { continuation ->
        instance.me { user, error ->
            continuation.resumeUserInfo(user, error)
        }
    }

    /**
     * 카카오 사용자 정보 요청
     */
    suspend fun me(): Result<User> = runCatching {
        UserApiClient.me()
    }

    private fun Continuation<OAuthToken>.resumeTokenOrException(
        token: OAuthToken?, error: Throwable?
    ) {
        if (error != null) {
            resumeWithException(error)
        } else if (token != null) {
            resume(token)
        } else {
            resumeWithException(RuntimeException("토큰 접근 에러"))
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