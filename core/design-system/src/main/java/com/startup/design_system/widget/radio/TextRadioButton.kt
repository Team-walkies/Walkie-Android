package com.startup.design_system.widget.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable

@Composable
fun TextRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        WalkieRadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = WalkieTheme.typography.body2.copy(
                color = WalkieTheme.colors.gray700
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextRadioButton() {
    WalkieTheme {
        Column {
            TextRadioButton(
                text = "서비스 이용 불편",
                selected = true,
                onClick = { }
            )
            TextRadioButton(
                text = "더 나은 대안 발견",
                selected = false,
                onClick = { }
            )
            TextRadioButton(
                text = "사용 빈도 낮음",
                selected = false,
                onClick = { }
            )
        }
    }
}
