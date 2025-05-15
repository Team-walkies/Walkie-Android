package com.startup.model.egg

data class EggOfCharacterGainProbabilityModel(
    val eggKind: EggKind,
    val normalProbability : Float,
    val rareProbability: Float,
    val epicProbability: Float,
    val legendProbability: Float
)
