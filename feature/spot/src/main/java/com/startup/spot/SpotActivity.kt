package com.startup.spot

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.navigation.LoginModuleNavigator
import com.startup.design_system.ui.WalkieTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpotActivity : BaseActivity<SpotUiEvent, SpotNavigationEvent>() {
    override val viewModel: SpotViewModel by viewModels<SpotViewModel>()
    private val loginModuleNavigator: LoginModuleNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            LoginNavigatorEntryPoint::class.java
        ).loginNavigatorNavigator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebView.setWebContentsDebuggingEnabled(true)
            WalkieTheme {
                CookieManager.getInstance().removeAllCookies(null)
                SpotScreen(
                    event = viewModel.event.filterIsInstance<SpotEvent>(),
                    uiEvent = ::handleUiEvent
                )
                handleNavigationEvent(viewModel.event.filterIsInstance())
            }
        }
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<SpotNavigationEvent>) {
        navigationEventFlow.onEach {
            when (it) {
                SpotNavigationEvent.FinishSpotActivity -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                SpotNavigationEvent.Logout -> {
                    loginModuleNavigator.navigateLoginView(context = this)
                    finish()
                }
                SpotNavigationEvent.FinishAndRefreshSpotActivity -> {
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun handleUiEvent(uiEvent: SpotUiEvent) {
        viewModel.notifyViewModelEvent(uiEvent)
    }

    override fun navigateToLogin() {
        loginModuleNavigator.navigateLoginView(this)
        finish()
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LoginNavigatorEntryPoint {
        fun loginNavigatorNavigator(): LoginModuleNavigator
    }
}