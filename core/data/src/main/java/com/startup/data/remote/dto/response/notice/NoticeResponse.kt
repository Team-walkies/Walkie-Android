package com.startup.data.remote.dto.response.notice

import com.google.gson.annotations.SerializedName

data class NoticeResponse(
    @SerializedName("notices")
    val notices: List<NoticeDto>?
)