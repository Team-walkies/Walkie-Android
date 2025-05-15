package com.startup.model.egg

import com.startup.common.util.DateUtil
import com.startup.domain.model.egg.MyEgg
import com.startup.model.character.WalkieCharacter

data class MyEggModel(
    val eggId: Long,
    val needStep: Int,
    val nowStep: Int,
    val play: Boolean,
    val eggKind: EggKind,
    val obtainedDate: String,
    val obtainedPosition: String,
    val walkieCharacter: WalkieCharacter
) {
    companion object {
        fun List<MyEgg>.toUiModel(): List<MyEggModel> = map { it.toUiModel() }
        fun MyEgg.toUiModel(): MyEggModel = MyEggModel(
            eggId = eggId,
            needStep = needStep,
            nowStep = nowStep,
            play = play,
            eggKind = EggKind.rankOfEggKind(rank),
            obtainedDate = DateUtil.convertDateFormat(obtainedDate),
            obtainedPosition = obtainedPosition,
            walkieCharacter = WalkieCharacter.getTypeAndClassOfWalkieCharacter(
                characterId = characterId,
                rank = rank,
                clazz = characterClass,
                type = type,
                picked = false,
                count = 0
            )
        )

        fun List<MyEggModel>.currentPlayEgg(): MyEggModel? = find { it.play }
    }

}
