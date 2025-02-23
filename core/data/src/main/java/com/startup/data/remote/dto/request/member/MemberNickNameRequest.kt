package com.startup.data.remote.dto.request.member

import com.google.gson.annotations.SerializedName

data class MemberNickNameRequest(
    @SerializedName("memberNickname")
    val memberNickname: String
)
