package com.startup.data.datasource

import com.startup.data.remote.dto.response.character.AcquisitionCharacterInfoResponse
import com.startup.data.remote.dto.response.character.AcquisitionCharacterResponse
import com.startup.data.remote.dto.response.character.CharacterCountDto
import kotlinx.coroutines.flow.Flow

interface CharacterDataSource {
    fun getCharacterAcquisitionDetail(characterId: Long): Flow<AcquisitionCharacterInfoResponse>
    fun getOwnedCharacterList(type: Int): Flow<AcquisitionCharacterResponse>
    fun getOwnedCharacterCount(): Flow<CharacterCountDto>
}