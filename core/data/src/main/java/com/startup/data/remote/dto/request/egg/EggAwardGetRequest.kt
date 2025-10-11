package com.startup.data.remote.dto.request.egg

import com.google.gson.annotations.SerializedName

data class EggAwardGetRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("healthcareEggAcquiredAt")
    val healthcareEggAcquiredAt: String
)