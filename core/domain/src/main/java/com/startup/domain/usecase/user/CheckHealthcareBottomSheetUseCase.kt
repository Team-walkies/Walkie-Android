package com.startup.domain.usecase.user

import com.startup.domain.repository.UserRepository
import javax.inject.Inject

class CheckHealthcareBottomSheetUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun shouldShowHealthcareBottomSheet(): Boolean {
        val isRemoteConfigVisible = userRepository.isHealthcareGuideBottomSheetVisible()
        val isNotShownBefore = !userRepository.isHealthcareBottomSheetShown()
        return isRemoteConfigVisible && isNotShownBefore
    }

    suspend fun markHealthcareBottomSheetAsShown() {
        userRepository.setHealthcareBottomSheetShown(true)
    }
}