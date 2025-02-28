package com.startup.domain.model.egg

data class UpdateStepData(
    val eggId: Long,
    val nowStep: Int,
    val latitude: Double,
    val longitude: Double,
)
