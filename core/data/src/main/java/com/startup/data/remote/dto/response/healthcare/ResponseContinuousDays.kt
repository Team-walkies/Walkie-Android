package com.startup.data.remote.dto.response.healthcare

import com.google.gson.annotations.SerializedName

data class ResponseContinuousDays(
    @SerializedName("continuousDays")
    val continuousDays: Int?
)
