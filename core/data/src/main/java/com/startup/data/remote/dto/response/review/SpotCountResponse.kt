package com.startup.data.remote.dto.response.review

import com.google.gson.annotations.SerializedName

data class SpotCountResponse(
    @SerializedName("count")
    val count: Int?
)
