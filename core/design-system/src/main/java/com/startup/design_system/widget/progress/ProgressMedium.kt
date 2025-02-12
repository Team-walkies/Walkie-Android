package com.startup.design_system.widget.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme


@Composable
fun ProgressMedium(progress: Float) {
    Box(
        modifier = Modifier
            .height(8.dp)
            .background(color = WalkieTheme.colors.gray200, shape = RoundedCornerShape(100.dp))
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(WalkieTheme.colors.blue300, shape = RoundedCornerShape(100.dp))
        )
    }
}

@Preview
@Composable
fun PreviewProgressMedium() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            ProgressMedium(1F)
            ProgressMedium(0.5F)
            ProgressMedium(0F)
        }
    }
}