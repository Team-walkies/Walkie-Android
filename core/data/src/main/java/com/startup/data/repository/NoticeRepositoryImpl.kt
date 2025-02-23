package com.startup.data.repository

import com.startup.data.datasource.NoticeDataSource
import com.startup.domain.model.notice.Notice
import com.startup.domain.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class NoticeRepositoryImpl @Inject constructor(private val noticeDataSource: NoticeDataSource) :
    NoticeRepository {
    override fun getNoticeList(): Flow<List<Notice>> =
        noticeDataSource.getNoticeList().map { it.notices?.map { it.toDomain() } ?: emptyList() }
}