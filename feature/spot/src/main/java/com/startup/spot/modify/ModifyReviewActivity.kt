package com.startup.spot.modify

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.navigation.LoginModuleNavigator
import com.startup.spot.ModifyReviewEvent
import com.startup.spot.ModifyReviewNavigationEvent
import com.startup.spot.ModifyReviewUiEvent
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
class ModifyReviewActivity : BaseActivity<ModifyReviewUiEvent, ModifyReviewNavigationEvent>() {
    override val viewModel: ModifyReviewViewModel by viewModels<ModifyReviewViewModel>()
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
                BackHandler {}
                CookieManager.getInstance().removeAllCookies(null)
                ModifySpotScreen(
                    event = viewModel.event.filterIsInstance<ModifyReviewEvent>(),
                    uiEvent = ::handleUiEvent
                )
                handleNavigationEvent(viewModel.event.filterIsInstance())
            }
        }
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<ModifyReviewNavigationEvent>) {
        navigationEventFlow.onEach {
            when (it) {
                ModifyReviewNavigationEvent.FinishWithModifyActivity -> {
                    setResult(RESULT_OK)
                    finish()
                }

                ModifyReviewNavigationEvent.Finish -> {
                    setResult(RESULT_CANCELED)
                    finish()
                }

                ModifyReviewNavigationEvent.Logout -> {
                    navigateToLogin()
                }
            }
        }.launchIn(lifecycleScope)
    }


    override fun handleUiEvent(uiEvent: ModifyReviewUiEvent) {
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