package com.startup.home

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent

sealed interface MainScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToLoginActivity : MainScreenNavigationEvent
    data class MoveToSpotModifyActivity(
        val launcher: ActivityResultLauncher<Intent>,
        val intent: Intent.() -> Intent
    ) : MainScreenNavigationEvent

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
    data object MoveToServiceTerm : MyPageScreenNavigationEvent
    data object MoveToRequestUserOpinion : MyPageScreenNavigationEvent
    data class MoveToUnlink(val nickName: String) : MyPageScreenNavigationEvent
    data object MoveToLoginActivityWithLogout : MyPageScreenNavigationEvent
    data object MoveToNotification : MyPageScreenNavigationEvent
}


sealed interface MyPageUiEvent : UiEvent {
    data object OnClickLogout : MyPageUiEvent
}