package com.startup.data.remote.dto.request.character

import com.google.gson.annotations.SerializedName

data class CharacterIdRequest(
    @SerializedName("characterId")
    val characterId: Long
)