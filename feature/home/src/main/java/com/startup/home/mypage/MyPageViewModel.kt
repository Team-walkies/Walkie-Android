package com.startup.home.mypage

import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.domain.usecase.ChangeUserProfileVisibility
import com.startup.domain.usecase.GetArriveSpotNotiEnabled
import com.startup.domain.usecase.GetEggHatchedNotiEnabled
import com.startup.domain.usecase.GetMyData
import com.startup.domain.usecase.GetTodayStepNotiEnabled
import com.startup.domain.usecase.Logout
import com.startup.domain.usecase.Unlink
import com.startup.domain.usecase.UpdateArriveSpotNotiEnabled
import com.startup.domain.usecase.UpdateEggHatchedNotiEnabled
import com.startup.domain.usecase.UpdateTodayStepNotiEnabled
import com.startup.home.ErrorToastEvent
import com.startup.home.MainScreenNavigationEvent
import com.startup.home.R
import com.startup.home.mypage.model.MyInfoViewModelEvent
import com.startup.home.mypage.model.MyInfoViewState
import com.startup.home.mypage.model.MyInfoViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    getMyData: GetMyData,
    getArriveSpotNotiEnabled: GetArriveSpotNotiEnabled,
    getEggHatchedNotiEnabled: GetEggHatchedNotiEnabled,
    getTodayStepNotiEnabled: GetTodayStepNotiEnabled,
    private val changeUserProfileVisibility: ChangeUserProfileVisibility,
    private val updateArriveSpotNotiEnabled: UpdateArriveSpotNotiEnabled,
    private val updateEggHatchedNotiEnabled: UpdateEggHatchedNotiEnabled,
    private val updateTodayStepNotiEnabled: UpdateTodayStepNotiEnabled,
    private val unlink: Unlink,
    private val logout: Logout
) : BaseViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state: MyInfoViewStateImpl = MyInfoViewStateImpl(
        isNotificationEnabledEggHatched = getEggHatchedNotiEnabled
            .invoke(Unit)
            .stateInViewModel(false),

        isNotificationEnabledSpotArrive = getArriveSpotNotiEnabled
            .invoke(Unit)
            .stateInViewModel(false),

        isProfileAccess = merge(
            flow<Unit> { emit(Unit) },
            viewModelEvent.filter { it == MyInfoViewModelEvent.OnChangedProfileVisibility })
            .flatMapLatest { getMyData.invoke(Unit).map { it.isPublic } }
            .stateInViewModel(false),

        isNotificationEnabledTodayStep = getTodayStepNotiEnabled
            .invoke(Unit)
            .stateInViewModel(false),
        userInfo = getMyData
            .invoke(Unit)
            .catch { }
            .stateInViewModel(null)
    )
    override val state: MyInfoViewState get() = _state

    fun updateArriveSpotNoti(enabled: Boolean) {
        updateArriveSpotNotiEnabled
            .invoke(enabled)
            .launchIn(viewModelScope)
    }

    fun updateTodayStepNoti(enabled: Boolean) {
        updateTodayStepNotiEnabled
            .invoke(enabled)
            .launchIn(viewModelScope)
    }

    fun updateEggHatchedNoti(enabled: Boolean) {
        updateEggHatchedNotiEnabled
            .invoke(enabled)
            .launchIn(viewModelScope)
    }

    fun updateProfileAccess() {
        changeUserProfileVisibility
            .invoke(Unit)
            .onEach { notifyViewModelEvent(MyInfoViewModelEvent.OnChangedProfileVisibility) }
            .catch { Printer.e("LMH", "updateProfileAccess Error $it") }
            .launchIn(viewModelScope)
    }

    fun unLink() {
        unlink
            .invoke(Unit)
            .onEach {
                notifyEvent(MainScreenNavigationEvent.MoveToLoginActivity)
            }
            .catch {
                notifyEvent(ErrorToastEvent.ShowToast(R.string.toast_common_error))
            }
            .launchIn(viewModelScope)
    }

    fun logout() {
        logout
            .invoke(Unit)
            .onEach {
                notifyEvent(MainScreenNavigationEvent.MoveToLoginActivity)
            }
            .catch {
                notifyEvent(MainScreenNavigationEvent.MoveToLoginActivity)
            }
            .launchIn(viewModelScope)
    }
}