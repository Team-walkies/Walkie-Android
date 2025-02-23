package com.startup.data.remote.datasourceimpl

import com.startup.data.datasource.CharacterDataSource
import com.startup.data.remote.dto.response.character.AcquisitionCharacterInfoResponse
import com.startup.data.remote.dto.response.character.AcquisitionCharacterResponse
import com.startup.data.remote.dto.response.character.CharacterCountDto
import com.startup.data.remote.ext.emitRemote
import com.startup.data.remote.service.CharacterService
import com.startup.data.util.handleExceptionIfNeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class CharacterDataSourceImpl @Inject constructor(private val characterService: CharacterService) :
    CharacterDataSource {

    override fun getCharacterAcquisitionDetail(characterId: Long): Flow<AcquisitionCharacterInfoResponse> =
        flow {
            handleExceptionIfNeed {
                emitRemote(characterService.getCharacterAcquisitionDetail(characterId))
            }
        }

    override fun getOwnedCharacterList(type: Int): Flow<AcquisitionCharacterResponse> = flow {
        handleExceptionIfNeed {
            emitRemote(characterService.getOwnedCharacterList(type))
        }
    }

    override fun getOwnedCharacterCount(): Flow<CharacterCountDto> = flow {
        handleExceptionIfNeed {
            emitRemote(characterService.getOwnedCharacterCount())
        }
    }
}