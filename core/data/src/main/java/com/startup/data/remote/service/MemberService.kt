package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.request.character.CharacterIdRequest
import com.startup.data.remote.dto.request.egg.WalkingEggRequest
import com.startup.data.remote.dto.request.member.MemberNickNameRequest
import com.startup.data.remote.dto.response.character.CharacterDto
import com.startup.data.remote.dto.response.egg.EggDto
import com.startup.data.remote.dto.response.member.GetUserInfoResponse
import com.startup.data.remote.dto.response.member.IsPublicDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

internal interface MemberService {
    /** 내 정보 조회하기 */
    @GET("api/v1/members")
    suspend fun getUserInfo(): BaseResponse<GetUserInfoResponse>

    /** 내 정보 수정하기 */
    @PATCH("api/v1/members")
    suspend fun modifyUserInfo(@Body request: MemberNickNameRequest): BaseResponse<Unit>

    /** 회원탈퇴 하기 */
    @DELETE("api/v1/members")
    suspend fun withdrawalService(): BaseResponse<Int>

    /** 로그 아웃 하기 */
    @POST("api/v1/auth/logout")
    suspend fun logoutService(): BaseResponse<Unit>

    /** 같이 걷는 알 변경 API */
    @PATCH("api/v1/members/eggs/play")
    suspend fun updateWalkingEgg(@Body request: WalkingEggRequest): BaseResponse<Unit>

    /** 같이 걷는 알 조회 API */
    @GET("api/v1/members/eggs/play")
    suspend fun getWalkingEgg(): BaseResponse<EggDto>

    /** 같이 걷는 캐릭터 변경 API */
    @PATCH("api/v1/members/characters/play")
    suspend fun modifyWalkingCharacter(@Body request: CharacterIdRequest): BaseResponse<Unit>

    /** 같이 걷는 캐릭터 조회 API */
    @GET("api/v1/members/characters/play")
    suspend fun getWalkingCharacter(): BaseResponse<CharacterDto>

    /** 내 프로필 공개/비공개 토글 API, 요청하면 값이 반전 됨 */
    @PATCH("api/v1/members/profile/visibility")
    suspend fun changeUserProfileVisibility(): BaseResponse<IsPublicDto>

    /** 사용자가 기록한 스팟의 갯수 조회 **/
    @GET("api/v1/members/recorded-spot")
    suspend fun getRecordedSpotCount(): BaseResponse<Int>

}