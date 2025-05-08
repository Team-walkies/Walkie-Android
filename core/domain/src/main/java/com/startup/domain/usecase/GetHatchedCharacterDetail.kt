package com.startup.domain.usecase

import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.repository.CharacterRepository
import com.startup.domain.util.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHatchedCharacterDetail @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseUseCase<CharacterDetail, Long>() {
    override fun invoke(params: Long): Flow<CharacterDetail> =
        characterRepository.getCharacterAcquisitionDetail(params)
}