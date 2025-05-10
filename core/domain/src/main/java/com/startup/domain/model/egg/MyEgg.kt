package com.startup.domain.model.egg

data class MyEgg(
    val characterId: Long,
    val eggId: Long,
    val needStep: Int,
    val nowStep: Int,
    val play: Boolean,
    val rank: Int,
    val obtainedDate: String,
    val obtainedPosition: String,
    val type: Int,
    val characterClass: Int,
)