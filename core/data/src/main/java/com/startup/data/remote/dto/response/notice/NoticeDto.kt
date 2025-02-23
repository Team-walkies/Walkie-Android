package com.startup.data.remote.dto.response.notice

import com.google.gson.annotations.SerializedName
import com.startup.domain.model.notice.Notice

data class NoticeDto(
    @SerializedName("date")
    val date: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("detail")
    val detail: String?,
) {
    fun toDomain(): Notice = Notice(
        title = title.orEmpty(),
        detail = detail.orEmpty(),
        date = date.orEmpty()
    )
}