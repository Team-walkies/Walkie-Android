package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.egg.EggDetail

data class EggAwardsPostDto(
    @SerializedName("characterClass")
    val characterClass: Int?,
    @SerializedName("characterRank")
    val characterRank: Int?,
    @SerializedName("characterType")
    val characterType: Int?,
    @SerializedName("eggId")
    val eggId: Long?,
    @SerializedName("memberId")
    val memberId: Long?,
    @SerializedName("needStep")
    val needStep: Int?,
    @SerializedName("nowStep")
    val nowStep: Int?,
    @SerializedName("obtainedDate")
    val obtainedDate: String?,
    @SerializedName("obtainedPosition")
    val obtainedPosition: String?,
    @SerializedName("picked")
    val picked: Boolean?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("userCharacterId")
    val userCharacterId: Int?
) {
    fun toDomain() = EggDetail(
        eggId = eggId.orZero(),
        needStep = needStep.orZero(),
        nowStep = nowStep.orZero(),
        obtainedDate = obtainedDate.orEmpty(),
        obtainedPosition = obtainedPosition.orEmpty(),
        rank = rank.orZero(),
    )
}