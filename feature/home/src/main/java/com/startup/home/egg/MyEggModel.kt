package com.startup.home.egg

import com.startup.domain.model.egg.MyEgg

data class MyEggModel(
    val characterId: Long,
    val eggId: Long,
    val needStep: Int,
    val nowStep: Int,
    val play: Boolean,
    val eggKind: EggKind
) {
    companion object {
        fun MyEgg.toUiModel(): MyEggModel = MyEggModel(
            characterId = characterId,
            eggId = eggId,
            needStep = needStep,
            nowStep = nowStep,
            play = play,
            eggKind = EggKind.rankOfEggKind(rank)
        )
    }
}
