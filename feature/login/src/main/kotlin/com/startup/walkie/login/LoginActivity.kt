package com.startup.walkie.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.base.BaseActivity
import com.startup.navigation.LoginFeatureNavigator
import com.startup.ui.WalkieTheme
import com.startup.walkie.login.model.GetCharacterNavigationEvent
import com.startup.walkie.login.model.LoginNavigationEvent
import com.startup.walkie.login.model.LoginUiEvent
import com.startup.walkie.login.model.NickNameSettingEvent
import com.startup.walkie.navigation.LoginScreenNav
import com.startup.walkie.splash.SplashActivity.LoginFeatureNavigatorEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginUiEvent, LoginNavigationEvent>() {
    override val viewModel: LoginViewModel by viewModels()

    // KSP 는 필드 주입이 안 됨
    private val loginFeatureNavigator: LoginFeatureNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            LoginFeatureNavigatorEntryPoint::class.java
        ).loginFeatureNavigator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkieTheme {
                MainContent()
            }
        }
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<LoginNavigationEvent>) {

    }

    override fun handleUiEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            LoginUiEvent.OnClickLoginButton -> {
                viewModel.onLogin()
            }
        }
    }

    private fun handleNickNameSettingUiEvent(uiEvent: NickNameSettingEvent) {
        when (uiEvent) {
            is NickNameSettingEvent.OnNickNameChanged -> {
                viewModel.onNickNameChanged(uiEvent.nickNameTextFieldValue)
            }

            else -> {

            }
        }
    }


    @Composable
    fun MainContent() {
        val navController = rememberNavController()
        val snackBarHostState = SnackbarHostState()
        WalkieTheme {

            Scaffold(
                snackbarHost = {
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WalkieTheme.colors.white)
                        .windowInsetsPadding( // 내비게이션 바 인셋을 처리
                            WindowInsets.systemBars.only(WindowInsetsSides.Vertical)
                        ),
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = LoginScreenNav.Onboarding.route
                    ) {
                        composable(LoginScreenNav.Onboarding.route) {
                            LoginScreen(uiEventSender = ::handleUiEvent)
                        }
                        composable(LoginScreenNav.NickNameSetting.route) {
                            NickNameSettingScreen(
                                viewModel.state,
                                viewEvent = ::handleNickNameSettingUiEvent
                            )
                        }
                        composable(
                            route = LoginScreenNav.GetEgg.route + "/{nickName}",
                            arguments = listOf(
                                navArgument("nickName") { type = NavType.StringType },
                            )
                        ) { navBackStackEntry ->
                            GetCharacterScreen(
                                navBackStackEntry.arguments?.getString("nickName").orEmpty()
                            ) {
                                when (it) {
                                    GetCharacterNavigationEvent.MoveToMainActivity -> {
                                        loginFeatureNavigator.moveToHomeActivity(this@LoginActivity)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}