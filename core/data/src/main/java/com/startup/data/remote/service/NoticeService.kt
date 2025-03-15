package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.notice.NoticeResponse
import retrofit2.http.GET

internal interface NoticeService {
    /** 공지사항 리스트 조회 API */
    @GET("api/v1/notices")
    suspend fun getNoticeList(): BaseResponse<NoticeResponse>
}