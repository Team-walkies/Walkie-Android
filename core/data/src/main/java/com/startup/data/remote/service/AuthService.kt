package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.auth.JoinRequest
import com.startup.data.remote.dto.request.auth.LoginRequest
import com.startup.data.remote.dto.response.auth.TokenDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthService {
    @POST("api/v1/auth")
    suspend fun login(@Body request: LoginRequest): BaseResponse<TokenDto>

    @POST("api/v1/auth/join")
    suspend fun join(@Body request: JoinRequest): BaseResponse<TokenDto>

    @POST("api/v1/auth")
    suspend fun refreshTokenUpdate(@Header("Authorization") authorization: String): BaseResponse<TokenDto>
}