package com.startup.domain.usecase.notice

import com.startup.domain.model.notice.Notice
import com.startup.domain.repository.NoticeRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoticeList @Inject constructor(private val noticeRepository: NoticeRepository) :
    BaseUseCase<List<Notice>, Unit>() {
    override fun invoke(params: Unit): Flow<List<Notice>> = noticeRepository.getNoticeList()
}