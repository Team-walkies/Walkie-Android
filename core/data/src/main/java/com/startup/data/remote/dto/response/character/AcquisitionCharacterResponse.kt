package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName

data class AcquisitionCharacterResponse(
    @SerializedName("characters")
    val characters: List<AcquisitionCharacterDto>?
)
