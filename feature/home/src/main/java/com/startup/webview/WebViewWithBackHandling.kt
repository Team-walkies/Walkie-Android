package com.startup.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.webview.CommonWebView
import com.startup.design_system.ui.WalkieTheme

@Composable
fun <T> WebViewWithBackHandling(
    url: String,
    title: String,
    onNavigationEvent: (T) -> Unit,
    backEvent: T,
    modifier: Modifier = Modifier
) {
    var webViewCanGoBack by remember { mutableStateOf(false) }
    var webViewGoBack by remember { mutableStateOf({}) }

    // 실제 뒤로가기 이벤트 처리
    val handleBackPress = {
        if (webViewCanGoBack) {
            webViewGoBack()
        } else {
            onNavigationEvent(backEvent)
        }
    }

    // 시스템 뒤로가기 버튼 처리
    BackHandler {
        handleBackPress()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleActionBarType(
                {
                    handleBackPress()
                },
                title = title
            )
        )
        CommonWebView(
            url = url,
            modifier = Modifier.fillMaxSize(),
            onBackPressed = { canGoBack, webView ->
                webViewCanGoBack = canGoBack
                webViewGoBack = { webView?.goBack() }
            }
        )
    }
}