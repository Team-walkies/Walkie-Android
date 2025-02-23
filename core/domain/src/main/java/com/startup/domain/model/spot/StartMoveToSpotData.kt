package com.startup.domain.model.spot

/** 스팟으로 이동 시작하기 */
data class StartMoveToSpotData(
    val spotId: Long,
    val memberId: Long,
    val eggId: Long,
    val characterId: Long,
    val startLatitude: Double,
    val startLongitude: Double,
    val startTime: String
)
