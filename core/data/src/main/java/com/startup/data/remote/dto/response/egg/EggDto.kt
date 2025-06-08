package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.egg.MyEgg

data class EggDto(
    @SerializedName("characterId")
    val characterId: Long?,
    @SerializedName("eggId")
    val eggId: Long?,
    @SerializedName("needStep")
    val needStep: Int?,
    @SerializedName("nowStep")
    val nowStep: Int?,
    @SerializedName("play")
    val play: Boolean?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("characterRank")
    val characterRank: Int?,
    @SerializedName("characterType")
    val type: Int?,
    @SerializedName("characterClass")
    val clazz: Int?,
    @SerializedName("obtainedDate")
    val obtainedDate: String?,
    @SerializedName("obtainedPosition")
    val obtainedPosition: String?,
) {
    fun toDomain() = MyEgg(
        characterId = characterId.orZero(),
        eggId = eggId.orZero(),
        rank = rank ?: -1,
        characterRank = characterRank ?: -1,
        needStep = needStep.orZero(),
        nowStep = nowStep.orZero(),
        play = play.orFalse(),
        obtainedDate = obtainedDate.orEmpty(),
        obtainedPosition = obtainedPosition.orEmpty(),
        characterClass = clazz.orZero(),
        type = type.orZero(),
    )
}