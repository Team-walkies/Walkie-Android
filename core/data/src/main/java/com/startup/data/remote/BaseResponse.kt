package com.startup.data.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse<T : Any?>(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?
)
