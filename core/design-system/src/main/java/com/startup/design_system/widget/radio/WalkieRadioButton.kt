package com.startup.design_system.widget.radio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable

@Composable
fun WalkieRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val clickableModifier = if (onClick != null) {
        modifier.noRippleClickable { onClick() }
    } else {
        modifier
    }

    val blue300 = WalkieTheme.colors.blue300
    val white = WalkieTheme.colors.white
    val gray200 = WalkieTheme.colors.gray200

    Canvas(
        modifier = clickableModifier.size(24.dp)
    ) {
        val canvasSize = size.width
        val center = Offset(canvasSize / 2, canvasSize / 2)
        val outerRadius = canvasSize / 2

        if (selected) {
            drawCircle(
                color = blue300,
                radius = outerRadius,
                center = center
            )
            val innerRadius = 5.dp.toPx()
            drawCircle(
                color = white,
                radius = innerRadius,
                center = center
            )
        } else {
            drawCircle(
                color = gray200,
                radius = outerRadius,
                center = center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWalkieRadioButton() {
    WalkieTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WalkieRadioButton(
                selected = false,
                onClick = { }
            )
            WalkieRadioButton(
                selected = true,
                onClick = { }
            )
        }
    }
}
