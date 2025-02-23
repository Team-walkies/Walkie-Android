package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName

data class EggCountResponse(
    @SerializedName("eggsCount")
    val eggsCount: Int?
)
