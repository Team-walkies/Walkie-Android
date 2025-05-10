package com.startup.domain.model.spot

data class SpotWebPostRequest(
    val accessToken: String,
    val characterRank: Int,
    val characterType: Int,
    val characterClass: Int,
    val characterId: Long,
)
