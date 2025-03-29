package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orFalse
import com.startup.common.extension.orZero
import com.startup.domain.model.character.MyCharacterWithWalk

data class CharacterDto(
    @SerializedName("characterId")
    val characterId: Long?,
    /** 캐릭터 종류 */
    @SerializedName("type")
    val type: Int?,
    /** 캐릭터 소분류 */
    @SerializedName("characterClass")
    val characterClazz: Int?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("picked")
    val picked: Boolean?,
) {
    fun toDomain() = MyCharacterWithWalk(
        characterId = characterId.orZero(),
        type = type.orZero(),
        clazz = characterClazz.orZero(),
        rank = rank.orZero(),
        count = count.orZero(),
        picked = picked.orFalse()
    )
}