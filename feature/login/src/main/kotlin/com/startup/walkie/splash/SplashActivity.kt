package com.startup.walkie.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.startup.common.base.BaseActivity
import com.startup.common.util.EXTRA_NEED_TO_NICKNAME_SETTING
import com.startup.login.R
import com.startup.navigation.HomeModuleNavigator
import com.startup.ui.WalkieTheme
import com.startup.walkie.login.LoginActivity
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
class SplashActivity : BaseActivity<SplashUiEvent, SplashNavigationEvent>() {
    override val viewModel: SplashViewModel by viewModels<SplashViewModel>()


    // KSP 는 필드 주입이 안 됨
    private val homeModuleNavigator: HomeModuleNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            HomeModuleNavigatorEntryPoint::class.java
        ).homeModuleNavigator()
    }

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
                    homeModuleNavigator.moveToHomeActivity(this)
                    finish()
                }

                SplashNavigationEvent.MoveToOnBoarding -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }

                SplashNavigationEvent.MoveToOnBoardingAndNickNameSet -> {
                    startActivity(Intent(this, LoginActivity::class.java).apply {
                        putExtra(EXTRA_NEED_TO_NICKNAME_SETTING, true)
                    })
                    finish()
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun handleUiEvent(uiEvent: SplashUiEvent) {}


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HomeModuleNavigatorEntryPoint {
        fun homeModuleNavigator(): HomeModuleNavigator
    }
}

@Composable
private fun SplashScreenCompose() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "앱 로고"
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