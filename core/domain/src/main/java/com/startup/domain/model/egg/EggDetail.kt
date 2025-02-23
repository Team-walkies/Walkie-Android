package com.startup.domain.model.egg

data class EggDetail(
    val eggId: Long,
    val rank: Int,
    val needStep: Int,
    val nowStep: Int,
    val obtainedDate: String,
    val obtainedPosition: String,
)
