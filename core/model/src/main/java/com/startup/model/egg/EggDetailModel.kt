package com.startup.model.egg

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
        fun List<EggDetail>.toUiModel(): List<EggDetailModel> = map {
            it.toUiModel()
        }

        private fun EggDetail.toUiModel(): EggDetailModel = EggDetailModel(
            eggId = eggId,
            needStep = needStep,
            nowStep = nowStep,
            eggKind = com.startup.model.egg.EggKind.rankOfEggKind(rank),
            obtainedPosition = obtainedPosition,
            obtainedDate = DateUtil.convertDateTimeFormat(obtainedDate)
        )
    }
}
