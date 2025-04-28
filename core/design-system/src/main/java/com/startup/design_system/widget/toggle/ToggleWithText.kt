package com.startup.design_system.widget.toggle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

@Composable
fun ToggleWithText(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = title,
                style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subTitle,
                style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500)
            )
        }
        Toggle(checked = checked, onCheckedChanged = onCheckedChanged)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewToggleWithText() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .background(WalkieTheme.colors.white)
                .padding(10.dp)
        ) {
            ToggleWithText(
                title = "프로필 공개",
                subTitle = "내 후기가 다른 사람에게 공개 돼요",
                checked = true,
                onCheckedChanged = {}
            )
        }
    }
}