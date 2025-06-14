package com.startup.login.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.login.R
import com.startup.login.login.model.GetGiftNavigationEvent
import kotlinx.coroutines.delay

@Composable
fun GetGiftScreen(nickName: String, onNavigationEvent: (GetGiftNavigationEvent) -> Unit) {
    val imageAndButtonAlpha = remember { Animatable(0f) }
    val titleTextAlpha = remember { Animatable(0f) }
    val subTitleTextAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        imageAndButtonAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOut)
        )

        delay(600)
        titleTextAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOut)
        )

        delay(600)
        subTitleTextAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300, easing = EaseOut)
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
            .padding(horizontal = 16.dp)
    ) {
        val screenHeight = maxHeight
        val imageSize = screenHeight * 0.4f
        
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 82.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(imageSize)
                    .graphicsLayer(alpha = imageAndButtonAlpha.value),
                painter = painterResource(R.drawable.img_gift),
                contentDescription = stringResource(R.string.desc_onboarding_gift)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.get_gift_introduce, nickName),
                style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700),
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer(alpha = titleTextAlpha.value)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.get_gift_introduce_sub_title, "해파리"),
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.blue400),
                modifier = Modifier.graphicsLayer(alpha = subTitleTextAlpha.value)
            )
        }
        Box(
            modifier = Modifier
                .noRippleClickable {
                    onNavigationEvent.invoke(GetGiftNavigationEvent.MoveToMainActivity)
                }
                .padding(bottom = 28.dp)
                .graphicsLayer(alpha = imageAndButtonAlpha.value)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.blue300,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 15.dp),
                text = stringResource(R.string.get_gift_action_button, "해파리"),
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.white),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
@PreviewScreenSizes
@Preview
fun PreviewGetCharacterScreen() {
    WalkieTheme {
        GetGiftScreen(nickName = "승빈짱짱") {}
    }
}