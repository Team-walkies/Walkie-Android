package com.startup.domain.usecase.character

import com.startup.domain.model.character.CharacterDetail
import com.startup.domain.repository.CharacterRepository
import com.startup.domain.usecase.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHatchedCharacterDetail @Inject constructor(private val characterRepository: CharacterRepository) :
    BaseUseCase<CharacterDetail, Long>() {
    override fun invoke(params: Long): Flow<CharacterDetail> =
        characterRepository.getCharacterAcquisitionDetail(params)
}