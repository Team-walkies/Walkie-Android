package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.auth.TokenDto
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthService {
    @POST("api/v1/auth")
    suspend fun login(@Header("[Token]]") authorization: String): BaseResponse<TokenDto>

    @POST("api/v1/auth")
    suspend fun refreshTokenUpdate(@Header("Authorization") authorization: String): BaseResponse<TokenDto>
}