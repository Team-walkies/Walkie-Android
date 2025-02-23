package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.egg.EggDetail

data class EggDetailDto(
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("needStep")
    val needStep: Int?,
    @SerializedName("nowStep")
    val nowStep: Int?,
    @SerializedName("obtainedDate")
    val obtainedDate: String?,
    @SerializedName("obtainedPosition")
    val obtainedPosition: String?,
) {
    fun toDomain(eggId: Long): EggDetail = EggDetail(
        eggId = eggId,
        rank = rank.orZero(),
        needStep = needStep.orZero(),
        nowStep = nowStep.orZero(),
        obtainedDate = obtainedDate.orEmpty(),
        obtainedPosition = obtainedPosition.orEmpty()
    )
}