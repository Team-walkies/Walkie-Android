package com.startup.spot.modify

import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.startup.common.util.Printer
import com.startup.spot.BuildConfig
import com.startup.spot.ModifyReviewEvent
import com.startup.spot.ModifyReviewUiEvent
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.flow.Flow

@Composable
internal fun ModifySpotScreen(
    event: Flow<ModifyReviewEvent>,
    uiEvent: (ModifyReviewUiEvent) -> Unit
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
    webView.clearHistory()
    webView.clearCache(true)
    LaunchedEffect(Unit) {
        uiEvent.invoke(ModifyReviewUiEvent.LoadWebViewParams)
        event.collect { event ->
            when (event) {
                is ModifyReviewEvent.LoadWebView -> {
                    val data = event.modifyReviewWebPostRequest
                    val url =
                        BuildConfig.BASE_SPOT_MODIFY_URL + "?reviewId=${data.reviewId}&spotId=${data.spotId}&token=${data.accessToken}"
                    Printer.e("LMH", "URL $url")
                    webView.loadUrl(url)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)
    ) {
        AndroidView(
            factory = {
                webView.apply {
                    settings.run {
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
                    addJavascriptInterface(ModifyReviewBridgeJsInterface {
                        uiEvent.invoke(it)
                    }, "AndroidBridge")
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                }
            },
            update = {

            }
        )
    }
}


internal class ModifyReviewBridgeJsInterface(
    private val bridgeCallBack: (ModifyReviewUiEvent) -> Unit
) {
    @JavascriptInterface
    fun finishReviewModify() {
        bridgeCallBack.invoke(ModifyReviewUiEvent.FinishReviewModify)

    }

    @JavascriptInterface
    fun finishWebView() {
        bridgeCallBack.invoke(ModifyReviewUiEvent.FinishWebView)
    }
}