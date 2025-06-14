package com.startup.login.login

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.startup.common.base.BaseActivity
import com.startup.common.base.BaseEvent
import com.startup.common.util.Printer
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.modal.ErrorModal
import com.startup.login.R
import com.startup.login.login.model.GetGiftNavigationEvent
import com.startup.login.login.model.LoginNavigationEvent
import com.startup.login.login.model.LoginScreenNavigationEvent
import com.startup.login.login.model.LoginUiEvent
import com.startup.login.login.model.NickNameSettingEvent
import com.startup.login.navigation.LoginScreenNav
import com.startup.navigation.HomeModuleNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<LoginUiEvent, LoginNavigationEvent>() {
    override val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var homeModuleNavigator: HomeModuleNavigator

    private val kaKaoLoginClient by lazy { KaKaoLoginClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalkieTheme {
                MainContent(viewModel.event)
                val errorDialog by viewModel.errorDialog.collectAsStateWithLifecycle()
                if (errorDialog != null) {
                    ErrorModal(
                        title = stringResource(R.string.dialog_user_account_withdrawn_exception_title),
                        subTitle = stringResource(R.string.dialog_user_account_withdrawn_exception_subtitle),
                        positiveText = stringResource(R.string.dialog_positive),
                        onDismiss = {
                            viewModel.onClearErrorDialog()
                        },
                        onClickPositive = {
                            viewModel.onClearErrorDialog()
                        }
                    )
                }
            }
        }
        handleNavigationEvent(viewModel.event.filterIsInstance())
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<LoginNavigationEvent>) {
        navigationEventFlow.onEach {
            when (it) {
                LoginNavigationEvent.MoveToMainActivity -> {
                    homeModuleNavigator.moveToHomeActivity(this)
                    finish()
                }

                else -> {
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun handleUiEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            LoginUiEvent.OnClickLoginButton -> {

                lifecycleScope.launch {
                    kaKaoLoginClient.login(this@LoginActivity).fold(
                        onSuccess = { token ->
                            viewModel.onLogin(token.accessToken)
                        },
                        onFailure = { error ->
                            Printer.e("LMH", "로그인 오류 : $error")
                        }
                    )
                }
            }

            else -> {}
        }
    }

    private fun handleNickNameSettingUiEvent(uiEvent: NickNameSettingEvent) {
        when (uiEvent) {
            is NickNameSettingEvent.OnNickNameChanged -> {
                viewModel.onNickNameChanged(uiEvent.nickNameTextFieldValue)
            }

            is NickNameSettingEvent.OnClickNickNameConfirm -> {
                viewModel.onJoinWalkie(uiEvent.nickName)
            }

            else -> {}
        }
    }


    @Composable
    fun MainContent(eventFlow: Flow<BaseEvent>) {
        val navController = rememberNavController()
        // 이벤트 수집
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                eventFlow.collect { event ->
                    Printer.e("LMH", "GET NAVIGATION EVENT $event")
                    when (event) {
                        is LoginScreenNavigationEvent.MoveToNickNameSettingScreen -> {
                            navController.navigate(LoginScreenNav.NickNameSetting.route)
                        }

                        is LoginScreenNavigationEvent.MoveToGetGiftScreen -> {
                            navController.navigate(LoginScreenNav.GetCharacter.route + "/${event.nickName}")
                        }
                    }
                }
            }
        }
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
                            route = LoginScreenNav.GetCharacter.route + "/{nickName}",
                            arguments = listOf(
                                navArgument("nickName") { type = NavType.StringType },
                            )
                        ) { navBackStackEntry ->
                            GetGiftScreen(
                                navBackStackEntry.arguments?.getString("nickName").orEmpty()
                            ) {
                                when (it) {
                                    GetGiftNavigationEvent.MoveToMainActivity -> {
                                        homeModuleNavigator.moveToHomeActivity(this@LoginActivity)
                                        finish() // 홈 이동 이후, 로그인 액티비티 스택에서 제거
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}