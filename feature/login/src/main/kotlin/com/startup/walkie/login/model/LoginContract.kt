package com.startup.walkie.login.model

import androidx.compose.ui.text.input.TextFieldValue
import com.startup.common.base.BaseState
import com.startup.common.base.NavigationEvent
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface LoginNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : LoginNavigationEvent
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
}

sealed interface NickNameNavigationEvent : NavigationEvent {

}

interface NickNameViewState : BaseState {
    val nickName: StateFlow<TextFieldValue>
    val placeHolder: StateFlow<String>
}

class NickNameViewStateImpl : NickNameViewState {
    override val nickName: MutableStateFlow<TextFieldValue> = MutableStateFlow(
        TextFieldValue(
            ""
        )
    )
    override val placeHolder: MutableStateFlow<String> = MutableStateFlow("닉네임")
}