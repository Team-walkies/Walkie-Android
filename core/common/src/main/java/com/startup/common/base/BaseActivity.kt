package com.startup.common.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.startup.common.util.SessionManager
import com.startup.common.util.SessionManagerEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseActivity<UE : UiEvent, NE : NavigationEvent> : ComponentActivity() {

    abstract val viewModel: BaseViewModel
    private val sessionManager: SessionManager by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            SessionManagerEntryPoint::class.java
        ).sessionManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sessionManager.sessionExpiredFlow.collect {
                    handleSessionExpired()
                }
            }
        }
    }

    private fun handleSessionExpired() {
        sessionManager.clearSessionState()
        navigateToLogin()
    }

    open fun navigateToLogin() {}

    abstract fun handleUiEvent(uiEvent: UE)

    abstract fun handleNavigationEvent(navigationEventFlow: Flow<NE>)

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}