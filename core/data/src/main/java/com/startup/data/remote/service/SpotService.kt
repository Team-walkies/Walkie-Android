package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.spot.SpotInfoResponse
import com.startup.data.remote.dto.request.spot.StartMoveToSpotRequest
import com.startup.data.remote.dto.response.spot.CurrentSpotIdResponse
import com.startup.data.remote.dto.response.spot.CurrentVisitantResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

internal interface SpotService {
    /** 스팟 검색하기 */
    @GET("/spots")
    suspend fun searchSpots(): BaseResponse<SpotInfoResponse>

    /** 스팟으로 이동 시작하기 */
    @POST("/spots")
    suspend fun startMoveToSpot(@Body request: StartMoveToSpotRequest): BaseResponse<CurrentSpotIdResponse>

    /** 스팟으로 이동 완료하기 */
    @PATCH("/spots/{spotId}")
    suspend fun completeMoveToSpot(@Path("spotId") spotId: Long): BaseResponse<CurrentSpotIdResponse>

    /** 스팟으로 이동 중지하기 */
    @PATCH("/spots/pause")
    suspend fun stopMoveToSpot(): BaseResponse<CurrentSpotIdResponse>

    /** 스팟별 방문자 조회하기 */
    @PATCH("/spots/curVisitant/{spotId}")
    suspend fun getSpotOfVisitor(@Path("spotId") spotId: Long): BaseResponse<CurrentVisitantResponse>
}