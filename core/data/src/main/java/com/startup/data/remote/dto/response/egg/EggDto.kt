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
) {
    fun toDomain() = MyEgg(
        characterId = characterId.orZero(),
        eggId = eggId.orZero(),
        rank = rank.orZero(),
        needStep = needStep.orZero(),
        nowStep = nowStep.orZero(),
        play = play.orFalse(),
    )
}