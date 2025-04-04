package com.startup.data.remote.dto.response.egg

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.egg.UpdateEggStepInfo

data class EggStepUpdateResponse(
    @SerializedName("eggId")
    val eggId: Long?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("needStep")
    val needStep: Int?,
    @SerializedName("nowStep")
    val nowStep: Int?,
    @SerializedName("obtainedPosition")
    val obtainedPosition: String?,
    @SerializedName("obtainedDate")
    val obtainedDate: String?,
    @SerializedName("picked")
    val picked: Boolean?,
    @SerializedName("userCharacterId")
    val userCharacterId: Long?,
    @SerializedName("memberId")
    val memberId: Long?
) {
    fun toDomain(): UpdateEggStepInfo = UpdateEggStepInfo(
        eggId = eggId.orZero(),
        rank = rank.orZero(),
        needStep = needStep.orZero(),
        nowStep = nowStep.orZero(),
        obtainedPosition = obtainedPosition.orEmpty(),
        obtainedDate = obtainedDate.orEmpty(),
        picked = picked ?: false,
        userCharacterId = userCharacterId.orZero(),
        memberId = memberId.orZero()
    )
}