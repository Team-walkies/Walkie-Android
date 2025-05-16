package com.startup.domain.usecase.character

import com.startup.domain.repository.CharacterRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHatchedCharacterCount @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = characterRepository.getOwnedCharacterCount()
}