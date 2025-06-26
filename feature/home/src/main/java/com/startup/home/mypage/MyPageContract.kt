package com.startup.home.mypage

import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseUiState
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent
import com.startup.domain.model.member.UserInfo
import com.startup.domain.model.notice.Notice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MyInfoViewState : BaseState {
    val isProfileAccess: StateFlow<Boolean>
    val isNotificationEnabledSpotArrive: StateFlow<Boolean>
    val isNotificationEnabledEggHatched: StateFlow<Boolean>
    val isNotificationEnabledTodayStep: StateFlow<Boolean>
    val userInfo : StateFlow<BaseUiState<UserInfo>>
    val isGrantNotificationPermission: StateFlow<Boolean>
}

interface NoticeViewState : BaseState {
    val noticeList: StateFlow<List<Notice>>
}

class NoticeViewStateImpl : NoticeViewState {
    override val noticeList: MutableStateFlow<List<Notice>> = MutableStateFlow(emptyList())
}


class MyInfoViewStateImpl(
    override val isProfileAccess: StateFlow<Boolean>,
    override val isNotificationEnabledSpotArrive: StateFlow<Boolean>,
    override val isNotificationEnabledEggHatched: StateFlow<Boolean>,
    override val isNotificationEnabledTodayStep: StateFlow<Boolean>,
    override val userInfo: StateFlow<BaseUiState<UserInfo>>,
    override val isGrantNotificationPermission: MutableStateFlow<Boolean>
) : MyInfoViewState

sealed interface MyInfoUIEvent : UiEvent {
    data class OnChangedProfileAccessToggle(val enabled: Boolean) : MyInfoUIEvent
}
sealed interface MyInfoViewModelEvent : BaseEvent {
    data object OnChangedProfileVisibility: MyInfoViewModelEvent
}

sealed interface UnlinkUiEvent : UiEvent {
    data object UnlinkWalkie : UnlinkUiEvent
}

sealed interface NoticeScreenNavigationEvent : ScreenNavigationEvent {
    data class MoveToNoticeDetail(val notice: Notice) : NoticeScreenNavigationEvent
    data object Back : NoticeScreenNavigationEvent
}

sealed interface PushSettingUIEvent : UiEvent {
    data class OnChangedTodayStepNoti(val enabled: Boolean) : PushSettingUIEvent
    data class OnChangedArriveSpotNoti(val enabled: Boolean) : PushSettingUIEvent
    data class OnChangedEggHatchedNoti(val enabled: Boolean) : PushSettingUIEvent
    data object OnClickMoveNotificationSetting : PushSettingUIEvent
}