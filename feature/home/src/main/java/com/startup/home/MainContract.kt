package com.startup.home

import com.startup.common.base.ScreenNavigationEvent

sealed interface MainScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToLoginActivity : MainScreenNavigationEvent
    data object MoveToSpotActivity : MainScreenNavigationEvent
}

sealed interface HomeScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToGainEgg : HomeScreenNavigationEvent
    data object MoveToGainCharacter : HomeScreenNavigationEvent
    data object MoveToNotification : HomeScreenNavigationEvent
    data object MoveToSpotArchive : HomeScreenNavigationEvent
}

sealed interface MyPageScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToMyInfo : MyPageScreenNavigationEvent
    data object MoveToPushSetting : MyPageScreenNavigationEvent
    data object MoveToNotice : MyPageScreenNavigationEvent
    data object MoveToPersonalInfoPolicy : MyPageScreenNavigationEvent
    data object MoveToRequestUserOpinion : MyPageScreenNavigationEvent
    data object MoveToUnlink : MyPageScreenNavigationEvent
    data object MoveToLoginActivity : MyPageScreenNavigationEvent
}