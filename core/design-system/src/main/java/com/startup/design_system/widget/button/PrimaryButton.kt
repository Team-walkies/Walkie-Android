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
import com.startup.ui.WalkieTheme

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
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
            containerColor = WalkieTheme.colors.blue300,
            disabledContentColor = WalkieTheme.colors.gray400,
            disabledContainerColor = WalkieTheme.colors.gray200
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, style = WalkieTheme.typography.body1)
    }
}

@Composable
@Preview
fun PreviewPrimaryButton() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            PrimaryButton("버튼", true) {}
            PrimaryButton("버튼", false) {}
        }
    }
}