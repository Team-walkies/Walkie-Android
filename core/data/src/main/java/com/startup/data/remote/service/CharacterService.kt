package com.startup.data.remote.service

import com.startup.data.remote.BaseResponse
import com.startup.data.remote.dto.response.character.AcquisitionCharacterInfoResponse
import com.startup.data.remote.dto.response.character.AcquisitionCharacterResponse
import com.startup.data.remote.dto.response.character.CharacterCountDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CharacterService {
    /** 캐릭터 획득 정보 상세 조회 API */
    @GET("/characters/details/{characterId}")
    suspend fun getCharacterAcquisitionDetail(@Path("characterId") characterId: Long): BaseResponse<AcquisitionCharacterInfoResponse>

    /** 보유한 캐릭터 리스트 조회 API */
    @GET("/characters")
    suspend fun getOwnedCharacterList(@Query("type") type: Int): BaseResponse<AcquisitionCharacterResponse>

    /** 보유한 캐릭터 갯수 조회 API */
    @GET("/characters/count")
    suspend fun getOwnedCharacterCount(): BaseResponse<CharacterCountDto>
}