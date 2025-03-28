package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName

data class EggCountResponse(
    @SerializedName("eggCount")
    val eggCount: Int?
)
