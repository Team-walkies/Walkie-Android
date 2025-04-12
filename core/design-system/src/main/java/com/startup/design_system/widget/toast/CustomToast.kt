package com.startup.design_system.widget.toast

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.delay

@Composable
private fun ToastOverlay(
    message: String,
    duration: Int,
    durationCallBack: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    if (isVisible) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 86.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1F))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WalkieTheme.colors.gray700, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                text = message,
                style = WalkieTheme.typography.body2,
                color = Color.White,
            )
        }

        // 자동으로 사라지게 설정
        LaunchedEffect(message) {
            delay(duration.toLong())
            isVisible = false
            durationCallBack.invoke()
        }
    }
}

@Composable
private fun IconToastOverlay(
    @DrawableRes iconResId: Int,
    message: String,
    duration: Int = Toast.LENGTH_LONG,
    tint: Color,
    durationCallBack: () -> Unit
) {
    var isVisible by remember { mutableStateOf(true) }
    if (isVisible) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 86.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.weight(1F))
            Row(
                modifier = Modifier
                    .background(WalkieTheme.colors.gray700, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(iconResId), tint = tint, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = message,
                    style = WalkieTheme.typography.body2,
                    color = Color.White,
                )
            }
        }

        // 자동으로 사라지게 설정
        LaunchedEffect(message) {
            delay(duration.toLong())
            isVisible = false
            durationCallBack.invoke()
        }
    }
}

@Composable
fun ShowToast(message: String, duration: Int, durationCallBack: () -> Unit) {
    ToastOverlay(message, duration, durationCallBack)
}

@Composable
fun ShowToast(message: String, duration: Int, iconResId: Int, tint: Color, durationCallBack: () -> Unit) {
    IconToastOverlay(iconResId, message, duration, tint, durationCallBack)
}

@Preview
@Composable
private fun PreviewCustomToastContent() {
    WalkieTheme {
        Box {
            IconToastOverlay(
                iconResId = R.drawable.ic_check,
                message = "This is a preview message1",
                tint = WalkieTheme.colors.blue300,
                duration = 2000
            ) {}
        }
    }
}