package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.character.MyCharacter

data class AcquisitionCharacterDto(
    @SerializedName("characterId")
    val characterId: Long?,
    @SerializedName("characterClass")
    val characterClass: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("picked")
    val picked: Boolean?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("type")
    val type: Int?,
) {
    fun toDomain() = MyCharacter(
        characterId = characterId.orZero(),
        characterClass = characterClass.orZero(),
        count = count.orZero(),
        picked = picked.orFalse(),
        rank = rank.orZero(),
        type = type.orZero()
    )
}