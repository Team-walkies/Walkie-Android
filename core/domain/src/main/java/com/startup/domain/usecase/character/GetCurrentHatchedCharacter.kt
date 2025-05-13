package com.startup.domain.usecase.character

import com.startup.domain.model.character.MyCharacter
import com.startup.domain.repository.CharacterRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentHatchedCharacter @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseUseCase<List<MyCharacter>, Int>() {
    override fun invoke(params: Int): Flow<List<MyCharacter>> =
        characterRepository.getOwnedCharacterList(params)
            .map { it.sortedWith(compareBy<MyCharacter> { character -> character.rank }.thenBy { character -> character.characterClass }) }
}