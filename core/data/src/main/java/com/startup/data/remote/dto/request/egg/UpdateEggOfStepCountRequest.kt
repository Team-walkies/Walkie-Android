package com.startup.data.remote.dto.request.egg

import com.google.gson.annotations.SerializedName

data class UpdateEggOfStepCountRequest(
    @SerializedName("eggId")
    val eggId: Long,
    @SerializedName("nowStep")
    val nowStep: Int,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
)
