package com.startup.spot

import android.Manifest
import android.webkit.GeolocationPermissions
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.viewinterop.AndroidView
import com.startup.common.util.Printer
import com.startup.common.util.UsePermissionHelper
import com.startup.common.util.rememberPermissionRequestDelegator
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

@Composable
internal fun SpotScreen(
    event: Flow<SpotEvent>,
    uiEvent: (SpotUiEvent) -> Unit
) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    val haptic = LocalHapticFeedback.current
    webView.clearHistory()
    webView.clearCache(true)
    LaunchedEffect(Unit) {
        event.filterIsInstance<SpotEvent>().collect { event ->
            when (event) {
                is SpotEvent.LoadWebView -> {
                    val data = event.spotWebPostRequestData
                    val url =
                        BuildConfig.BASE_SPOT_URL + "?accessToken=${data.accessToken}&memberId=${data.userId}&characterRank=${data.characterRank}&characterType=${data.characterType}&characterClass=${data.characterClass}"
                    Printer.e("LMH", "URL $url")
                    webView.loadUrl(url)
                }

                SpotEvent.Haptic -> {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }

                is SpotEvent.RequestCurrentSteps -> {
                    // TODO @IslandOrDream 스팟 탐험 완료 후 걸음 수 데이터 전달이 필요하여 요청, 이후 걸음 수 측정 종료
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)
    ) {
        var locationPermissionCallback: GeolocationPermissions.Callback? = null
        var originStr: String? = null
        val resultLauncher = rememberPermissionRequestDelegator(
            permissions = listOf(UsePermissionHelper.Permission.FOREGROUND_LOCATION),
            doOnGranted = { locationPermissionCallback?.invoke(originStr, true, true) },
            doOnNeverAskAgain = {},
            doOnShouldShowRequestPermissionRationale = {})

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
                    addJavascriptInterface(SpotBridgeJsInterface {
                        uiEvent.invoke(it)
                        if (it == SpotUiEvent.RequestCurrentSteps) {
                            sendToWebView(webView, method = "getStepsFromMobile", message = "300")
                        }
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }, "AndroidBridge")
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        override fun onGeolocationPermissionsShowPrompt(
                            origin: String?,
                            callback: GeolocationPermissions.Callback?
                        ) {
                            if (UsePermissionHelper.isGrantedPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                                // 권한이 이미 승인된 경우
                                callback?.invoke(origin, true, true)
                                Printer.e("LMH", "권한이 이미 승인된 경우")
                            } else {
                                locationPermissionCallback = callback
                                originStr = origin
                                resultLauncher?.requestPermissionLauncher()
                            }
                        }
                    }
                }
            },
            update = {

            }
        )
    }
}

internal fun sendToWebView(webView: WebView, method: String, message: String) {
    webView.evaluateJavascript("javascript:$method('$message')", null)
}

internal class SpotBridgeJsInterface(
    private val bridgeCallBack: (SpotUiEvent) -> Unit
) {
    @JavascriptInterface
    fun haptic() {
        bridgeCallBack.invoke(SpotUiEvent.Haptic)
    }

    @JavascriptInterface
    fun startCountingSteps() {
        bridgeCallBack.invoke(SpotUiEvent.StartCountingSteps)
    }

    @JavascriptInterface
    fun pressBackBtn() {
        bridgeCallBack.invoke(SpotUiEvent.PressBackBtn)
    }

    @JavascriptInterface
    fun finishReview() {
        bridgeCallBack.invoke(SpotUiEvent.FinishReview)

    }

    @JavascriptInterface
    fun logout() {
        bridgeCallBack.invoke(SpotUiEvent.Logout)
    }

    @JavascriptInterface
    fun finishWebView() {
        bridgeCallBack.invoke(SpotUiEvent.FinishWebView)
    }

    @JavascriptInterface
    fun requestCurrentSteps() {
        bridgeCallBack.invoke(SpotUiEvent.RequestCurrentSteps)
    }
}