package com.startup.design_system.widget.permission

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable

@Composable
fun PermissionInduction(@StringRes title: Int, modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                WalkieTheme.colors.gray900Opacity70
            )
            .noRippleClickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_danger),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = WalkieTheme.colors.yellow100)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(title),
            style = WalkieTheme.typography.body2.copy(WalkieTheme.colors.white)
        )
    }
}

@Preview
@PreviewScreenSizes
@Composable
fun PreviewPermissionInductionAlert() {
    WalkieTheme {
        PermissionInduction(
            title = R.string.permission_activity_recognition_alert,
            modifier = Modifier.padding(top = 24.dp)
        ) {}
    }
}
