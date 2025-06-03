package com.startup.spot

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.design_system.ui.WalkieTheme
import com.startup.navigation.LoginModuleNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SpotActivity : BaseActivity<SpotUiEvent, SpotNavigationEvent>() {
    override val viewModel: SpotViewModel by viewModels<SpotViewModel>()

    @Inject
    lateinit var loginModuleNavigator: LoginModuleNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
            WalkieTheme {
                BackHandler {}
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
                    setResult(RESULT_OK)
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
}