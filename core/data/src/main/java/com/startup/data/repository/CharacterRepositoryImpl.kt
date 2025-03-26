package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.data.datasource.CharacterDataSource
import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.model.character.MyCharacter
import com.startup.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CharacterRepositoryImpl @Inject constructor(private val characterDataSource: CharacterDataSource) :
    CharacterRepository {
    override fun getCharacterAcquisitionDetail(characterId: Long): Flow<CharacterDetail> =
        characterDataSource.getCharacterAcquisitionDetail(characterId)
            .map { it.toDomain(characterId) }

    override fun getOwnedCharacterList(type: Int): Flow<List<MyCharacter>> =
        characterDataSource.getOwnedCharacterList(type)
            .map { it.characters?.map { it.toDomain() } ?: emptyList() }

    override fun getOwnedCharacterCount(): Flow<Int> =
        characterDataSource.getOwnedCharacterCount().map { it.charactersCount.orZero() }
}