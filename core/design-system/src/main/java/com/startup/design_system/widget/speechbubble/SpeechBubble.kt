package com.startup.design_system.widget.speechbubble

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.ui.WalkieTheme

@Composable
fun SpeechBubble(
    steps: String,
    modifier: Modifier = Modifier,

    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // 상단 말풍선 부분
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(36.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(WalkieTheme.colors.gray700)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "부화까지" 부분
                Text(
                    text = stringResource(R.string.home_until_hatching_step),
                    style = WalkieTheme.typography.body2,
                    color = WalkieTheme.colors.gray400
                )
                // "N걸음" 부분
                Text(
                    text = stringResource(R.string.current_step, steps),
                    style = WalkieTheme.typography.body2,
                    color = WalkieTheme.colors.white
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.speech_bubble_pointer),
            contentDescription = null,
            modifier = Modifier.offset(y = (-1).dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechBubblePreview() {
    SpeechBubble(steps = "10")
}
