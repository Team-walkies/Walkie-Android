package com.startup.data.datasource

import com.startup.data.remote.dto.response.notice.NoticeResponse
import kotlinx.coroutines.flow.Flow

interface NoticeDataSource {
    fun getNoticeList(): Flow<NoticeResponse>
}