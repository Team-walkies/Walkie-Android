package com.startup.home.mypage.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.startup.common.base.NavigationEvent
import com.startup.home.R
import com.startup.webview.WalkieWebConstants
import com.startup.webview.WebViewWithBackHandling

@Composable
fun PersonalInfoPolicyScreen(
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    WebViewWithBackHandling(
        url = WalkieWebConstants.PRIVACY_POLICY_URL,
        title = stringResource(R.string.setting_policy),
        onNavigationEvent = onNavigationEvent,
        backEvent = NavigationEvent.Back
    )
}