package com.startup.login.login

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.provider.ResourceProvider
import com.startup.common.util.ClientException
import com.startup.common.util.ResponseErrorException
import com.startup.common.util.UserAccountWithdrawnException
import com.startup.common.util.UserAuthNotFoundException
import com.startup.common.util.WalkieException
import com.startup.domain.usecase.auth.JoinWalkie
import com.startup.domain.usecase.auth.LoginWalkie
import com.startup.login.R
import com.startup.login.login.model.LoginNavigationEvent
import com.startup.login.login.model.LoginScreenNavigationEvent
import com.startup.login.login.model.NickNameViewState
import com.startup.login.login.model.NickNameViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWalkie: LoginWalkie,
    private val joinWalkie: JoinWalkie,
    resourceProvider: ResourceProvider
) : BaseViewModel() {
    private val _state =
        NickNameViewStateImpl(placeHolder = MutableStateFlow(resourceProvider.getString(R.string.onboarding_nick_name_placeholder)))
    override val state: NickNameViewState = _state

    private val _errorDialog: MutableStateFlow<WalkieException?> = MutableStateFlow(null)
    val errorDialog: StateFlow<WalkieException?> = _errorDialog

    fun onClearErrorDialog() {
        _errorDialog.update { null }
    }

    fun onNickNameChanged(textFieldValue: TextFieldValue) {
        _state.nickName.update { textFieldValue }
    }

    fun onLogin(kakaoToken: String) {
        loginWalkie.invoke(kakaoToken)
            .catch { exception ->
                if (exception is UserAuthNotFoundException) {
                    _state.providerToken.update { exception.providerToken }
                    exception.nickName?.let { nickName ->
                        _state.placeHolder.update { nickName }
                    }
                    notifyEvent(LoginScreenNavigationEvent.MoveToNickNameSettingScreen)
                } else if ((exception is ResponseErrorException && exception.code == 401) || (exception is HttpException && exception.code() == 401) || (exception is ClientException && exception.code == 401)) {
                    _errorDialog.update { UserAccountWithdrawnException() }
                }
            }.onEach {
                notifyEvent(LoginNavigationEvent.MoveToMainActivity)
            }
            .launchIn(viewModelScope)
    }

    fun onJoinWalkie(nickName: String) {
        _state.providerToken.value?.let { providerToken ->
            joinWalkie.invoke(providerToken to nickName).onEach {
                notifyEvent(LoginScreenNavigationEvent.MoveToGetGiftScreen(nickName))
            }.catch { }.launchIn(viewModelScope)
        }
    }
}