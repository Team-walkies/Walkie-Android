package com.startup.domain.usecase

import com.startup.domain.repository.CharacterRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHatchedCharacterCount @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseUseCase<Int, Unit>() {
    override fun invoke(params: Unit): Flow<Int> = characterRepository.getOwnedCharacterCount()
}