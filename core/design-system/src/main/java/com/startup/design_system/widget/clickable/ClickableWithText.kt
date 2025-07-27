package com.startup.design_system.widget.clickable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.resource.R

@Composable
fun ClickableWithText(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(color = WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
            .noRippleClickable { onClick() }
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
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron),
            contentDescription = null,
            tint = WalkieTheme.colors.gray400,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClickableWithText() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .background(WalkieTheme.colors.white)
                .padding(10.dp)
        ) {
            ClickableWithText(
                title = "닉네임 변경",
                subTitle = "다이노해파리",
                onClick = {}
            )
        }
    }
}