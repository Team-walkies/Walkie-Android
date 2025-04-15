package com.startup.design_system.widget.webview

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CommonWebView(
    url: String,
    modifier: Modifier = Modifier,
    onBackPressed: (canGoBack: Boolean, webView: WebView?) -> Unit = { _, _ -> }
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
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

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onBackPressed(view?.canGoBack() ?: false, view)
                    }
                }

                webChromeClient = WebChromeClient()
                loadUrl(url)
                onBackPressed(canGoBack(), this)
            }
        },
        update = { view ->
            // URL이 변경된 경우에만 업데이트
            if (view.url != url) {
                view.loadUrl(url)
            }
            onBackPressed(view.canGoBack(), view)
        }
    )
}