package com.startup.home.egg.model

import com.startup.common.util.DateUtil
import com.startup.domain.model.egg.EggDetail

data class EggDetailModel(
    val eggId: Long,
    val eggKind: EggKind,
    val needStep: Int,
    val nowStep: Int,
    val obtainedDate: String,
    val obtainedPosition: String,
) {
    companion object {
        fun EggDetail.toUiModel(): EggDetailModel = EggDetailModel(
            eggId = eggId,
            needStep = needStep,
            nowStep = nowStep,
            eggKind = EggKind.rankOfEggKind(rank),
            obtainedPosition = obtainedPosition,
            obtainedDate = DateUtil.convertDateTimeFormat(obtainedDate)
        )
    }
}
