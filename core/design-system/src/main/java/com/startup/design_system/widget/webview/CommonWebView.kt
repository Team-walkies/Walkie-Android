package com.startup.design_system.widget.webview

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CommonWebView(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    displayZoomControls = false
                    builtInZoomControls = true
                    textZoom = 100
                }
                
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                
                // URL 로딩
                loadUrl(url)
            }
        },
        update = { webView ->
            // 상태 변경 시 WebView 업데이트
            webView.loadUrl(url)
        }
    )
}