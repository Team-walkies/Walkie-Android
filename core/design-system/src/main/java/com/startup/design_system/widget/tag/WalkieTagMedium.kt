package com.startup.design_system.widget.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme

@Composable
fun WalkieTagMedium(text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .background(WalkieTheme.colors.gray100, shape = RoundedCornerShape(100.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text, style = WalkieTheme.typography.head6.copy(color = textColor))
    }
}

@Preview
@Composable
fun PreviewWalkieTagMedium() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .background(WalkieTheme.colors.white)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WalkieTagMedium("일반", textColor = WalkieTheme.colors.blue500)
            WalkieTagMedium("희귀", textColor = WalkieTheme.colors.green300)
            WalkieTagMedium("에픽", textColor = WalkieTheme.colors.orange300)
            WalkieTagMedium("전설", textColor = WalkieTheme.colors.purple300)
        }
    }
}