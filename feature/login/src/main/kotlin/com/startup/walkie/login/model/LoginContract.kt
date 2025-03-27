package com.startup.walkie.login.model

import androidx.compose.ui.text.input.TextFieldValue
import com.startup.common.base.BaseState
import com.startup.common.base.NavigationEvent
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface LoginNavigationEvent : NavigationEvent {
    data object MoveToMainActivity : LoginNavigationEvent
}
sealed interface LoginScreenNavigationEvent : ScreenNavigationEvent {
    data object MoveToNickNameSettingScreen: LoginNavigationEvent
    data class MoveToGetCharacterScreen(val nickName: String): LoginNavigationEvent
}


sealed interface GetCharacterNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : GetCharacterNavigationEvent
}

sealed interface LoginUiEvent : UiEvent {
    data object OnClickLoginButton : LoginUiEvent
}

sealed interface NickNameSettingEvent : UiEvent {
    data class OnNickNameChanged(val nickNameTextFieldValue: TextFieldValue) :
        NickNameSettingEvent
    data class OnClickNickNameConfirm(val nickName: String): NickNameSettingEvent
}

sealed interface NickNameNavigationEvent : NavigationEvent {

}

interface NickNameViewState : BaseState {
    val nickName: StateFlow<TextFieldValue>
    val placeHolder: StateFlow<String>
    val providerToken: StateFlow<String?>
}

class NickNameViewStateImpl : NickNameViewState {
    override val nickName: MutableStateFlow<TextFieldValue> = MutableStateFlow(
        TextFieldValue(
            ""
        )
    )
    override val placeHolder: MutableStateFlow<String> = MutableStateFlow("닉네임")
    override val providerToken: MutableStateFlow<String?> = MutableStateFlow(null)
}