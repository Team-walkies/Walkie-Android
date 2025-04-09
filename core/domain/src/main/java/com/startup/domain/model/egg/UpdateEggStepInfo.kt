package com.startup.domain.model.egg

data class UpdateEggStepInfo(
    val eggId: Long,
    val rank: Int,
    val needStep: Int,
    val nowStep: Int,
    val obtainedPosition: String,
    val obtainedDate: String,
    val picked: Boolean,
    val userCharacterId: Long,
    val memberId: Long
)