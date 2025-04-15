package com.startup.home.mypage

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.startup.common.base.NavigationEvent
import com.startup.home.R
import com.startup.webview.WalkieWebConstants
import com.startup.webview.WebViewWithBackHandling

@Composable
fun RequestUserOpinionScreen(
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    WebViewWithBackHandling(
        url = WalkieWebConstants.REQUEST_USER_OPINION_URL,
        title = stringResource(R.string.request_opinion_title),
        onNavigationEvent = onNavigationEvent,
        backEvent = NavigationEvent.Back
    )
}