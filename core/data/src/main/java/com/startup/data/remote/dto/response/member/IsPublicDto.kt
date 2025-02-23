package com.startup.data.remote.dto.response.member

import com.google.gson.annotations.SerializedName

data class IsPublicDto(
    @SerializedName("isPublic")
    val isPublic: Boolean?
)
