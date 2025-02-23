package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.NoticeDataSource
import com.startup.data.remote.dto.response.notice.NoticeResponse
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.NoticeService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class NoticeDataSourceImpl @Inject constructor(private val noticeService: NoticeService) :
    NoticeDataSource {
    override fun getNoticeList(): Flow<NoticeResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(noticeService.getNoticeList())
        }
    }
}