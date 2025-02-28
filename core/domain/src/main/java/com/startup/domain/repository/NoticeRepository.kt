package com.startup.domain.repository

import com.startup.domain.model.notice.Notice
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    fun getNoticeList(): Flow<List<Notice>>
}