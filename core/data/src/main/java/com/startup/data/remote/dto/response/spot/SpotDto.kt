package com.startup.data.remote.dto.response.spot

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.spot.Spot

data class SpotDto(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("spotId")
    val spotId: Long?,
    @SerializedName("spotName")
    val spotName: String?
) {
    fun toDomain() = Spot(
        latitude = latitude.orZero(),
        longitude = longitude.orZero(),
        spotId = spotId.orZero(),
        spotName = spotName.orEmpty()
    )
}