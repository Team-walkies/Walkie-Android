package com.startup.domain.model.spot

data class Spot(
    val latitude: Double,
    val longitude: Double,
    val spotId: Long,
    val spotName: String
)