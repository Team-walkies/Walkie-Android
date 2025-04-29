package com.startup.home.mypage

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.startup.common.base.NavigationEvent
import com.startup.home.R
import com.startup.webview.WalkieWebConstants
import com.startup.webview.WebViewWithBackHandling

@Composable
fun ServiceTermScreen(
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    WebViewWithBackHandling(
        url = WalkieWebConstants.TERMS_OF_SERVICE_URL,
        title = stringResource(R.string.setting_service_term),
        onNavigationEvent = onNavigationEvent,
        backEvent = NavigationEvent.Back
    )
}