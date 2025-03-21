package com.startup.walkie.login

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.walkie.login.model.NickNameViewState
import com.startup.walkie.login.model.NickNameViewStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    private val _state = NickNameViewStateImpl(viewModelScope = viewModelScope)
    override val state: NickNameViewState = _state

    fun onNickNameChanged(textFieldValue: TextFieldValue) {
        _state.nickName.update { textFieldValue }
    }

    fun onLogin() {

    }
}