package com.startup.domain.repository

import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.model.character.MyCharacter
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacterAcquisitionDetail(characterId: Long): Flow<CharacterDetail>
    fun getOwnedCharacterList(type: Int): Flow<List<MyCharacter>>
    fun getOwnedCharacterCount(): Flow<Int>
}