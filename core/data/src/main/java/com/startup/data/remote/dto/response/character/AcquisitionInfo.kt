package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName
import com.startup.domain.model.character.CharacterObtainInfo

data class AcquisitionInfo(
    @SerializedName("obtainedPosition")
    val obtainedPosition: String?,
    /** yyyy-MM-dd */
    @SerializedName("obtainedDate")
    val obtainedDate: String?,
) {
    fun toDomain() = CharacterObtainInfo(
        obtainedPosition = obtainedPosition.orEmpty(),
        obtainedDate = obtainedDate.orEmpty()
    )
}