package com.startup.login.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.modal.PrimaryModal
import com.startup.login.R
import com.startup.login.login.LoginActivity
import com.startup.navigation.HomeModuleNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashUiEvent, SplashNavigationEvent>() {
    override val viewModel: SplashViewModel by viewModels<SplashViewModel>()

    @Inject
    lateinit var homeModuleNavigator: HomeModuleNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkieTheme {
                SplashScreenCompose(viewModel.state.isForceUpdateDialogShow) {
                    when (it) {
                        SplashUiEvent.RedirectPlayStore -> {
                            redirectToPlayStore()
                        }
                    }
                }
            }
        }
        handleNavigationEvent(viewModel.event.filterIsInstance())
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<SplashNavigationEvent>) {
        navigationEventFlow.onEach {
            when (it) {
                SplashNavigationEvent.MoveToMainActivity -> {
                    homeModuleNavigator.moveToHomeActivity(context = this)
                    finish()
                }

                SplashNavigationEvent.MoveToOnBoarding -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun handleUiEvent(uiEvent: SplashUiEvent) {}

    private fun redirectToPlayStore() {
        try {
            val appPackageName = packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$appPackageName")
                setPackage("com.android.vending") // Play Store 앱에서만 열기
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Play Store 앱이 없을 경우 브라우저로 열기
            val appPackageName = packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            }
            kotlin.runCatching {
                startActivity(intent)
            }
        }
    }
}

@Composable
private fun SplashScreenCompose(isForceUpdateDialogShowFlow: StateFlow<Boolean>, uiEvent: (SplashUiEvent) -> Unit) {
    val isForceUpdateDialogShow by isForceUpdateDialogShowFlow.collectAsStateWithLifecycle(false)
    BoxWithConstraints(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        val screenHeight = maxHeight
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height((240.dp / 780.dp) * screenHeight))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "앱 로고",
                modifier = Modifier.height(200.dp)
            )
        }
        if (isForceUpdateDialogShow) {
            PrimaryModal(
                title = stringResource(R.string.dialog_force_update_title),
                subTitle = stringResource(R.string.dialog_force_update_sub_title),
                positiveText = stringResource(R.string.dialog_force_update_positive_btn),
                backOrOutSideDismissBlock = true,
                onClickPositive = { uiEvent.invoke(SplashUiEvent.RedirectPlayStore) },
                onDismiss = {}
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun PreviewSplashScreenCompose() {
    WalkieTheme {
        SplashScreenCompose(MutableStateFlow(false)) {}
    }
}