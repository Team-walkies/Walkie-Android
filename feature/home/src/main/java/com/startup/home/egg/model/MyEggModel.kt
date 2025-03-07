package com.startup.home.egg.model

import com.startup.common.util.DateUtil
import com.startup.domain.model.egg.MyEgg

data class MyEggModel(
    val characterId: Long,
    val eggId: Long,
    val needStep: Int,
    val nowStep: Int,
    val play: Boolean,
    val eggKind: EggKind,
    val obtainedDate: String,
    val obtainedPosition: String,
) {
    companion object {
        fun MyEgg.toUiModel(): MyEggModel = MyEggModel(
            characterId = characterId,
            eggId = eggId,
            needStep = needStep,
            nowStep = nowStep,
            play = play,
            eggKind = EggKind.rankOfEggKind(rank),
            obtainedDate = DateUtil.convertDateFormat(obtainedDate),
            obtainedPosition = obtainedPosition
        )
        fun List<MyEggModel>.currentPlayEgg(): MyEggModel? = find { it.play }
    }

}
