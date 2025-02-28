package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName
import com.startup.common.extension.orZero
import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.model.character.CharacterObtainInfo

data class AcquisitionCharacterInfoResponse(
    @SerializedName("characterCount")
    val characterCount: Int?,
    @SerializedName("rank")
    val rank: Int?,
    @SerializedName("obtainedDetails")
    val obtainedDetails: List<AcquisitionInfo>?,
) {
    fun toDomain(characterId: Long) = CharacterDetail(
        characterId = characterId,
        characterCount = characterCount.orZero(),
        rank = rank.orZero(),
        obtainInfo = obtainedDetails?.toDomain().orEmpty()
    )

    private fun List<AcquisitionInfo>.toDomain(): List<CharacterObtainInfo> = map { it.toDomain() }
}
