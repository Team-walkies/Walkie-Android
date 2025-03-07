package com.startup.home.egg.model

data class EggOfCharacterGainProbabilityModel(
    val eggKind: EggKind,
    val normalProbability : Float,
    val rareProbability: Float,
    val epicProbability: Float,
    val legendProbability: Float
)
