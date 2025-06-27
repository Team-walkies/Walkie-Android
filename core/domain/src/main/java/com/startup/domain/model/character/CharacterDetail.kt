package com.startup.domain.model.character

data class CharacterDetail(
    val characterId: Long,
    val type: Int,
    val characterClass: Int,
    val characterCount: Int,
    val rank: Int,
    val characterDescription: String,
    // 네임은 아직 사용 X
    val characterName: String,
    val characterImageUrl: String,
    val obtainInfo: List<CharacterObtainInfo>
)
