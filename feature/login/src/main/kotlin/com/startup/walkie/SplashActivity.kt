package com.startup.walkie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.startup.login.R
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreenCompose()
        }
        checkUserLogin()
    }

    private fun checkUserLogin() {
        lifecycleScope.launch {
            delay(2000)
            // TODO 자동 로그인 여부 확인
            // TODO 첫 사용자 인지 확인
        }
    }
}

@Composable
fun SplashScreenCompose() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Spacer(Modifier.height(60.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "앱 로고"
            )
        }
    }
}


@PreviewScreenSizes
@Composable
fun PreviewSplashScreenCompose() {
    WalkieTheme {
        SplashScreenCompose()
    }
}