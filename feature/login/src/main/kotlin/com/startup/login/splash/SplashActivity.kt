package com.startup.login.splash

import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.design_system.ui.WalkieTheme
import com.startup.login.R
import com.startup.login.login.LoginActivity
import com.startup.navigation.HomeModuleNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
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
            SplashScreenCompose()
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
}

@Composable
private fun SplashScreenCompose() {
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
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewSplashScreenCompose() {
    WalkieTheme {
        SplashScreenCompose()
    }
}