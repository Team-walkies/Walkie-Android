package com.startup.design_system.widget.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    percentage: Float, // 0f ~ 1f
    strokeWidth: Dp,
    foregroundColor: Color = WalkieTheme.colors.blue300,
    backgroundColor: Color = WalkieTheme.colors.gray100,
    centerContent: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth / 2)
        ) {
            val sweep = percentage.coerceIn(0f, 1f) * 360f
            val stroke = strokeWidth.toPx()
            val diameter = size.minDimension
            val topLeftOffset = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )

            // 배경 원형
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                topLeft = topLeftOffset,
                size = Size(diameter, diameter)
            )

            // 실제 값에 해당하는 포그라운드
            drawArc(
                color = foregroundColor,
                startAngle = -90f, // 위에서 시작
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                topLeft = topLeftOffset,
                size = Size(diameter, diameter)
            )
        }
        centerContent()
    }
}


@Composable
@Preview
private fun PreviewDonutChart() {
    WalkieTheme {
        DonutChart(
            modifier = Modifier.size(260.dp),
            percentage = 0.35F,
            foregroundColor = WalkieTheme.colors.blue300,
            backgroundColor = WalkieTheme.colors.gray100,
            strokeWidth = 26.dp
        ) {
            Text(
                text = "${(0.35F * 100).toInt()}%",
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.white)
            )
        }
    }
}