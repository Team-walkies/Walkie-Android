package com.startup.data.remote.dto.request.auth

import com.google.gson.annotations.SerializedName

data class JoinRequest(
    @SerializedName("provider")
    val provider: String,
    @SerializedName("loginAccessToken")
    val loginAccessToken: String,
    @SerializedName("nickname")
    val nickName: String
)