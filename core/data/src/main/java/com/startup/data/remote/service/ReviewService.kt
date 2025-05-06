package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.data.remote.dto.response.review.ReviewResponse
import com.startup.data.remote.dto.response.review.SpotCountResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ReviewService {

    /** 리뷰 수정 API */
    @POST("api/v1/reviews/{reviewId}")
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Int,
        @Body request: ModifyReviewRequest
    ): BaseResponse<Unit>

    /** 리뷰 삭제 API */
    @DELETE("api/v1/reviews/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: Int): BaseResponse<Int>

    /** 스팟 별 리뷰수 조회 API */
    @GET("api/v1/reviews/count/{spotId}")
    suspend fun getSpotOfReviews(@Path("spotId") spotId: Long): BaseResponse<SpotCountResponse>

    /** 캘린더 리뷰 리스트 조회 API */
    @GET("api/v1/reviews/calendar")
    suspend fun getCalendarReviewList(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): BaseResponse<ReviewResponse>

    /** 스팟의 리뷰 리스트 조회 API */
    @GET("api/v1/reviews/spots")
    suspend fun getSpotOfReviewList(@Query("spotId") spotId: Long): BaseResponse<ReviewResponse>
}