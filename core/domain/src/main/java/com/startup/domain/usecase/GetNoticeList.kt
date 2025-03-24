package com.startup.domain.usecase

import com.startup.domain.model.notice.Notice
import com.startup.domain.repository.NoticeRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoticeList @Inject constructor(private val noticeRepository: NoticeRepository) :
    BaseUseCase<List<Notice>, Unit>() {
    override fun invoke(params: Unit): Flow<List<Notice>> = noticeRepository.getNoticeList()
}