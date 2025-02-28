package com.startup.data.remote.dto.response.spot

import com.google.gson.annotations.SerializedName

data class SpotInfoResponse(
    @SerializedName("spots")
    val spots: List<SpotDto>?
)