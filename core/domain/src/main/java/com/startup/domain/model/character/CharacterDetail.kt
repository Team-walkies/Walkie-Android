package com.startup.domain.model.character

data class CharacterDetail(
    val characterId: Long,
    val type: Int,
    val characterClass: Int,
    val characterCount: Int,
    val rank: Int,
    val obtainInfo: List<CharacterObtainInfo>
)
