package com.startup.data.remote.dto.response.character

import com.google.gson.annotations.SerializedName

data class CharacterCountDto(
    @SerializedName("charactersCount")
    val charactersCount: Int?
)
