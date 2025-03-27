package com.startup.home.mypage

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.domain.usecase.GetArriveSpotNotiEnabled
import com.startup.domain.usecase.GetEggHatchedNotiEnabled
import com.startup.domain.usecase.GetProfileAccessEnabled
import com.startup.domain.usecase.GetTodayStepNotiEnabled
import com.startup.domain.usecase.UpdateArriveSpotNotiEnabled
import com.startup.domain.usecase.UpdateEggHatchedNotiEnabled
import com.startup.domain.usecase.UpdateProfileAccessEnabled
import com.startup.domain.usecase.UpdateTodayStepNotiEnabled
import com.startup.home.mypage.model.MyInfoViewState
import com.startup.home.mypage.model.MyInfoViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    getArriveSpotNotiEnabled: GetArriveSpotNotiEnabled,
    getEggHatchedNotiEnabled: GetEggHatchedNotiEnabled,
    getProfileAccessEnabled: GetProfileAccessEnabled,
    getTodayStepNotiEnabled: GetTodayStepNotiEnabled,
    private val updateArriveSpotNotiEnabled: UpdateArriveSpotNotiEnabled,
    private val updateEggHatchedNotiEnabled: UpdateEggHatchedNotiEnabled,
    private val updateProfileAccessEnabled: UpdateProfileAccessEnabled,
    private val updateTodayStepNotiEnabled: UpdateTodayStepNotiEnabled
) : BaseViewModel() {
    private val _state: MyInfoViewStateImpl = MyInfoViewStateImpl(
        isNotificationEnabledEggHatched = getEggHatchedNotiEnabled.invoke(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false),

        isNotificationEnabledSpotArrive = getArriveSpotNotiEnabled.invoke(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false),

        isProfileAccess = getProfileAccessEnabled.invoke(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false),

        isNotificationEnabledTodayStep = getTodayStepNotiEnabled.invoke(Unit)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false),
    )
    override val state: MyInfoViewState get() = _state

    fun updateArriveSpotNoti(enabled: Boolean) {
        updateArriveSpotNotiEnabled.invoke(enabled).launchIn(viewModelScope)
    }

    fun updateTodayStepNoti(enabled: Boolean) {
        updateTodayStepNotiEnabled.invoke(enabled).launchIn(viewModelScope)
    }

    fun updateEggHatchedNoti(enabled: Boolean) {
        updateEggHatchedNotiEnabled.invoke(enabled).launchIn(viewModelScope)
    }

    fun updateProfileAccess(enabled: Boolean) {
        updateProfileAccessEnabled.invoke(enabled).launchIn(viewModelScope)
    }
}