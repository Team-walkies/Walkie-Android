package com.startup.walkie.login

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.UserAuthNotFoundException
import com.startup.domain.usecase.JoinWalkie
import com.startup.domain.usecase.LoginWalkie
import com.startup.walkie.login.model.LoginScreenNavigationEvent
import com.startup.walkie.login.model.NickNameViewState
import com.startup.walkie.login.model.NickNameViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWalkie: LoginWalkie,
    private val joinWalkie: JoinWalkie
) : BaseViewModel() {
    private val _state = NickNameViewStateImpl()
    override val state: NickNameViewState = _state

    fun onNickNameChanged(textFieldValue: TextFieldValue) {
        _state.nickName.update { textFieldValue }
    }

    fun onLogin() {
        loginWalkie.invoke(Unit).catch { exception ->
            if (exception is UserAuthNotFoundException) {
                _state.providerToken.update { exception.providerToken }
                notifyEvent(LoginScreenNavigationEvent.MoveToNickNameSettingScreen)
            }
        }
    }

    fun onJoinWalkie(nickName: String) {
        _state.providerToken.value?.let { providerToken ->
            joinWalkie.invoke(providerToken to nickName).onEach {
                notifyEvent(LoginScreenNavigationEvent.MoveToGetCharacterScreen(nickName))
            }.catch {  }.launchIn(viewModelScope)
        }
    }
}