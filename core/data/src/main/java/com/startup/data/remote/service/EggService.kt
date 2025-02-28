package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.egg.EggCountResponse
import com.startup.data.remote.dto.response.egg.EggDetailDto
import com.startup.data.remote.dto.response.egg.MyEggResponse
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

internal interface EggService {
    /** 알 상세정보 조회 API */
    @GET("/eggs/{eggId}")
    suspend fun getEggDetailInfo(@Path("eggId") eggId: Long): BaseResponse<EggDetailDto>

    /** 알의 걸은 걸음수 업데이트 API */
    @PATCH("/eggs/steps")
    suspend fun updateEggOfStepCount(@Body request: UpdateEggOfStepCountRequest): BaseResponse<Unit>

    /** 보유한 알 리스트 조회 API */
    @GET("/eggs")
    suspend fun getMyEggList(): BaseResponse<MyEggResponse>

    /** 보유한 알 갯수 조회 API */
    @GET("/eggs/count")
    suspend fun getMyEggCount(): BaseResponse<EggCountResponse>

}