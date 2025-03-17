package com.startup.walkie.login.model

import androidx.compose.ui.text.input.TextFieldValue
import com.startup.common.base.NavigationEvent
import com.startup.common.base.ScreenNavigationEvent
import com.startup.common.base.State
import com.startup.common.base.UiEvent
import hasSpecialCharacters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


sealed interface LoginNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : LoginNavigationEvent
}


sealed interface GetCharacterNavigationEvent : ScreenNavigationEvent {
    data object MoveToMainActivity : GetCharacterNavigationEvent
}

sealed interface NickNameSettingEvent : UiEvent {
    data class OnNickNameChanged(val nickNameTextFieldValue: TextFieldValue) :
        NickNameSettingEvent
}

sealed interface NickNameNavigationEvent : NavigationEvent {

}

interface NickNameViewState : State {
    val nickName: StateFlow<TextFieldValue>
    val placeHolder: StateFlow<String>
    val nickNameContainedSpecialCharacters: StateFlow<Boolean>
}

class NickNameViewStateImpl(
    private val viewModelScope: CoroutineScope
) : NickNameViewState {
    override val nickName: MutableStateFlow<TextFieldValue> = MutableStateFlow(
        TextFieldValue(
            ""
        )
    )
    override val placeHolder: MutableStateFlow<String> = MutableStateFlow("닉네임")
    override val nickNameContainedSpecialCharacters: StateFlow<Boolean>
        get() = nickName.map { it.text.hasSpecialCharacters() }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
}