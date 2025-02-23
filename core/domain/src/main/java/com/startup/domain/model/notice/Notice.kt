package com.startup.domain.model.notice

data class Notice(
    /** 공지 날짜 : yyyy-MM-dd */
    val date: String,
    val title: String,
    val detail: String,
)