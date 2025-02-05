package com.startup.common.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.Flow

abstract class BaseActivity<UE : UiEvent, NE : NavigationEvent> : ComponentActivity() {

    abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    abstract fun handleUiEvent(uiEvent: UE)

    abstract fun handleNavigationEvent(navigationEventFlow: Flow<NE>)

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}