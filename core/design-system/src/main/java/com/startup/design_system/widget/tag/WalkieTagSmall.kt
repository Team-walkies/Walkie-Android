package com.startup.design_system.widget.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme
import com.startup.ui.dropCustomShadow

@Composable
fun WalkieTagSmall(text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .dropCustomShadow(
                shape = CircleShape,
                color = Color.Black.copy(0.05f),
                offsetX = 0.dp,
                offsetY = 4.dp,
                blur = 20.dp,
                spread = 0.dp
            )
            .background(WalkieTheme.colors.white, shape = RoundedCornerShape(100.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text, style = WalkieTheme.typography.caption1.copy(color = textColor))
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewWalkieTagSmall() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .background(WalkieTheme.colors.white)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WalkieTagSmall("일반", textColor = WalkieTheme.colors.blue500)
            WalkieTagSmall("희귀", textColor = WalkieTheme.colors.green300)
            WalkieTagSmall("에픽", textColor = WalkieTheme.colors.orange300)
            WalkieTagSmall("전설", textColor = WalkieTheme.colors.purple300)
        }
    }
}