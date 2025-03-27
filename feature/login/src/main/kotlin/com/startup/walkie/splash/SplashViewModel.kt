package com.startup.walkie.splash

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.base.BaseState
import com.startup.domain.usecase.GetMyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(getMyData: GetMyData) : BaseViewModel() {
    override val state: BaseState = object : BaseState {}

    init {
        getMyData.invoke(Unit).onEach {
            delay(2_000)
            if (it.memberNickName.isBlank()) {
                notifyEvent(SplashNavigationEvent.MoveToOnBoardingAndNickNameSet)
            } else {
                notifyEvent(SplashNavigationEvent.MoveToMainActivity)
            }
        }.catch {
            delay(2_000)
            notifyEvent(SplashNavigationEvent.MoveToOnBoarding)
        }.launchIn(viewModelScope)
    }
}