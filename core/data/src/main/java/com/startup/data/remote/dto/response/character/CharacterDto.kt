package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.character.MyCharacterWithWalk

data class CharacterDto(
    @SerializedName("characterId")
    val characterId: Int?,
    /** 캐릭터 종류 */
    @SerializedName("type")
    val type: Int?,
    /** 캐릭터 소분류 */
    @SerializedName("class")
    val clazz: Int?,
) {
    fun toDomain() = MyCharacterWithWalk(
        characterId = characterId.orZero(),
        type = type.orZero(),
        clazz = clazz.orZero()
    )
}