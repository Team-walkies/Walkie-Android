package com.startup.data.remote.dto.request.auth

import com.google.gson.annotations.SerializedName

data class RefreshRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)