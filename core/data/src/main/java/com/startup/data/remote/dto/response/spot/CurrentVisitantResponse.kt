package com.startup.data.remote.dto.response.spot

import com.google.gson.annotations.SerializedName

data class CurrentVisitantResponse(
    @SerializedName("curVisitant")
    val curVisitant: Int?
)
