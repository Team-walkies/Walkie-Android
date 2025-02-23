package com.startup.data.remote

import com.google.gson.annotations.SerializedName

data class BaseResponse<T : Any?>(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?
)
