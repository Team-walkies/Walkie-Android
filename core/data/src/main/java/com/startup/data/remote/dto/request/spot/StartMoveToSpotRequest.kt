package com.startup.data.remote.dto.request.spot

import com.google.gson.annotations.SerializedName

/** 스팟으로 이동 시작하기 */
data class StartMoveToSpotRequest(
    @SerializedName("spotId")
    val spotId: Long,
    @SerializedName("memberId")
    val memberId: Long,
    @SerializedName("eggId")
    val eggId: Long,
    @SerializedName("characterId")
    val characterId: Long,
    @SerializedName("startLatitude")
    val startLatitude: Double,
    @SerializedName("startLongitude")
    val startLongitude: Double,
    @SerializedName("startTime")
    val startTime: String
)
