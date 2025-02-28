package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName

data class MyEggResponse(
    @SerializedName("eggs")
    val eggs: List<EggDto>?
)