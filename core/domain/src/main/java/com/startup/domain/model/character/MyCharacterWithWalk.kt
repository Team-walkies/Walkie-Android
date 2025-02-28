package com.startup.domain.model.character

data class MyCharacterWithWalk(
    val characterId: Int,
    /** 캐릭터 종류 */
    val type: Int,
    /** 캐릭터 소분류 */
    val clazz: Int,
)
