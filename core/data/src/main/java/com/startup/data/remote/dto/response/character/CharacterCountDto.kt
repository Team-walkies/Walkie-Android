package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName

data class CharacterCountDto(
    @SerializedName("characterCount")
    val characterCount: Int?
)
