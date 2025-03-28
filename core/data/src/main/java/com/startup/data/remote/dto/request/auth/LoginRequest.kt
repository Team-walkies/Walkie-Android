package com.startup.data.remote.dto.request.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("provider")
    val provider: String,
    @SerializedName("loginAccessToken")
    val loginAccessToken: String
)