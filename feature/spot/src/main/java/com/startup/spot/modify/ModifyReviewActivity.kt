package com.startup.spot.modify

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
import com.startup.spot.BuildConfig
import com.startup.spot.ModifyReviewEvent
import com.startup.spot.ModifyReviewNavigationEvent
import com.startup.spot.ModifyReviewUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ModifyReviewActivity : BaseActivity<ModifyReviewUiEvent, ModifyReviewNavigationEvent>() {
    override val viewModel: ModifyReviewViewModel by viewModels<ModifyReviewViewModel>()

    @Inject
    lateinit var  loginModuleNavigator: LoginModuleNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
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
}