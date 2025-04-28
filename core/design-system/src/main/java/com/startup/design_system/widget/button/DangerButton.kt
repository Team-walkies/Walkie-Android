package com.startup.design_system.widget.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

@Composable
fun DangerButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        onClick = onClick,
        enabled = enabled,
        colors = ButtonColors(
            contentColor = WalkieTheme.colors.white,
            containerColor = WalkieTheme.colors.red100,
            disabledContentColor = WalkieTheme.colors.white,
            disabledContainerColor = WalkieTheme.colors.red50
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, style = WalkieTheme.typography.body1)
    }
}

@Composable
@Preview
fun PreviewDangerButton() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            DangerButton(text = "버튼", enabled = true) {}
            DangerButton(text = "버튼", enabled = false) {}
        }
    }
}