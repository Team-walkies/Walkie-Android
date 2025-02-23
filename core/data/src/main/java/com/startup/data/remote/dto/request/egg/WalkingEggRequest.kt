package com.startup.data.remote.dto.request.egg

import com.google.gson.annotations.SerializedName

data class WalkingEggRequest(
    @SerializedName("eggId")
    val eggId: Long
)