package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.auth.JoinRequest
import com.startup.data.remote.dto.request.auth.LoginRequest
import com.startup.data.remote.dto.request.auth.RefreshRequest
import com.startup.data.remote.dto.response.auth.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): BaseResponse<TokenDto>

    @POST("api/v1/auth/signup")
    suspend fun join(@Body request: JoinRequest): BaseResponse<TokenDto>

    @POST("api/v1/auth/refresh")
    suspend fun refreshTokenUpdate(@Body request: RefreshRequest): BaseResponse<TokenDto>
}