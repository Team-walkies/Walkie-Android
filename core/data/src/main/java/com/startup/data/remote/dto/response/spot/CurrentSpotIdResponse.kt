package com.startup.data.remote.dto.response.spot

import com.google.gson.annotations.SerializedName

data class CurrentSpotIdResponse(
    @SerializedName("curSpotId")
    val curSpotId: Long?
)
