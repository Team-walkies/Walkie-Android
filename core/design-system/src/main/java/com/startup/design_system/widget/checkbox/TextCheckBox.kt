package com.startup.design_system.widget.checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme

@Composable
fun TextCheckBox(text: String, checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    Row(modifier = Modifier.wrapContentWidth(), verticalAlignment = Alignment.CenterVertically) {
        WalkieCheckBox(checked = checked, onCheckedChanged = onCheckedChanged)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = WalkieTheme.typography.body2.copy(
                color = if (checked) WalkieTheme.colors.gray700 else WalkieTheme.colors.gray500
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextCheckBox() {
    WalkieTheme {
        Column {
            TextCheckBox("체크 박스 라벨", true) {}
            TextCheckBox("체크 박스 라벨", false) {}
        }
    }
}