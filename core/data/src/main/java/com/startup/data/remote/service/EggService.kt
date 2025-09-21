package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.egg.UpdateEggOfStepCountRequest
import com.startup.data.remote.dto.request.egg.EggAwardGetRequest
import com.startup.data.remote.dto.response.egg.DailyEggResponse
import com.startup.data.remote.dto.response.egg.EggCountResponse
import com.startup.data.remote.dto.response.egg.EggDetailDto
import com.startup.data.remote.dto.response.egg.EggStepUpdateResponse
import com.startup.data.remote.dto.response.egg.MyEggResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

internal interface EggService {

    @POST("api/v1/eggs/awards")
    suspend fun postEggGet(@Body request: EggAwardGetRequest): BaseResponse<Unit>

    /** 알 상세정보 조회 API */
    @GET("eggs/{eggId}")
    suspend fun getEggDetailInfo(@Path("eggId") eggId: Long): BaseResponse<EggDetailDto>

    /** 알의 걸은 걸음수 업데이트 API */
    @PATCH("api/v1/eggs/steps")
    suspend fun updateEggOfStepCount(@Body request: UpdateEggOfStepCountRequest): BaseResponse<EggStepUpdateResponse>

    /** 보유한 알 리스트 조회 API */
    @GET("api/v1/eggs")
    suspend fun getMyEggList(): BaseResponse<MyEggResponse>

    /** 보유한 알 갯수 조회 API */
    @GET("api/v1/eggs/count")
    suspend fun getMyEggCount(): BaseResponse<EggCountResponse>

    /** 하루에 한번 알 획득 API */
    @GET("api/v1/events/daily-egg")
    suspend fun getDailyEgg(): BaseResponse<DailyEggResponse>

}