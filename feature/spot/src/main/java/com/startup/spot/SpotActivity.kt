package com.startup.spot

import androidx.activity.viewModels
import com.startup.common.base.BaseActivity
import com.startup.common.base.BaseViewModel
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.navigation.LoginModuleNavigator
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class SpotActivity: BaseActivity<UiEvent, NavigationEvent>() {
    override val viewModel: BaseViewModel by viewModels<SpotViewModel>()
    private val loginModuleNavigator: LoginModuleNavigator by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            LoginNavigatorEntryPoint::class.java
        ).loginNavigatorNavigator()
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {

    }

    override fun handleUiEvent(uiEvent: UiEvent) {

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