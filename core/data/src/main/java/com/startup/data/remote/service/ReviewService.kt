package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.review.ModifyReviewRequest
import com.startup.data.remote.dto.response.review.ReviewResponse
import com.startup.data.remote.dto.response.review.SpotCountResponse
import com.startup.data.remote.dto.request.review.WriteReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ReviewService {

    /** 리뷰 작성 API */
    @POST("/reviews")
    suspend fun writeReview(@Body request: WriteReviewRequest): BaseResponse<Unit>

    /** 리뷰 수정 API */
    @POST("/reviews/{reviewId}")
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Long,
        @Body request: ModifyReviewRequest
    ): BaseResponse<Unit>

    /** 리뷰 삭제 API */
    @POST("/reviews/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: Long): BaseResponse<Unit>

    /** 스팟 별 리뷰수 조회 API */
    @GET("/reviews/count/{spotId}")
    suspend fun getSpotOfReviews(@Path("spotId") spotId: Long): BaseResponse<SpotCountResponse>

    /** 캘린더 리뷰 리스트 조회 API */
    @GET("/reviews/calendar")
    suspend fun getCalendarReviewList(@Query("date") date: String): BaseResponse<ReviewResponse>

    /** 스팟의 리뷰 리스트 조회 API */
    @GET("/reviews/spots")
    suspend fun getSpotOfReviewList(@Query("spotId") spotId: Long): BaseResponse<ReviewResponse>
}