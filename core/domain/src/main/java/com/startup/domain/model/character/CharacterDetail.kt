package com.startup.domain.model.character

data class CharacterDetail(
    val characterId: Long,
    val characterCount: Int,
    val rank: Int,
    val obtainInfo: List<CharacterObtainInfo>
)
