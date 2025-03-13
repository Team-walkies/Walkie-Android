package com.startup.spot

import androidx.activity.viewModels
import com.startup.common.base.BaseActivity
import com.startup.common.base.BaseViewModel
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class SpotActivity: BaseActivity<UiEvent, NavigationEvent>() {
    override val viewModel: BaseViewModel by viewModels<SpotViewModel>()

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {

    }

    override fun handleUiEvent(uiEvent: UiEvent) {

    }
}