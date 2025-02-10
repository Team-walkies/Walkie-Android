package com.startup.design_system.widget.toggle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme

@Composable
fun WalkieToggle(checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    Switch(
        modifier = Modifier
            .width(51.dp)
            .height(31.dp),
        checked = checked,
        onCheckedChange = onCheckedChanged,
        colors = SwitchColors(
            checkedIconColor = WalkieTheme.colors.blue300,
            checkedBorderColor = WalkieTheme.colors.blue300,
            checkedTrackColor = WalkieTheme.colors.blue300,
            checkedThumbColor = WalkieTheme.colors.white,
            uncheckedThumbColor = WalkieTheme.colors.gray400,
            uncheckedBorderColor = WalkieTheme.colors.gray400,
            uncheckedIconColor = WalkieTheme.colors.gray200,
            uncheckedTrackColor = WalkieTheme.colors.gray200,
            disabledUncheckedBorderColor = WalkieTheme.colors.gray300,
            disabledCheckedIconColor = WalkieTheme.colors.gray300,
            disabledCheckedThumbColor = WalkieTheme.colors.gray300,
            disabledCheckedTrackColor = WalkieTheme.colors.gray300,
            disabledCheckedBorderColor = WalkieTheme.colors.gray300,
            disabledUncheckedIconColor = WalkieTheme.colors.gray300,
            disabledUncheckedThumbColor = WalkieTheme.colors.gray300,
            disabledUncheckedTrackColor = WalkieTheme.colors.gray300
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWalkieToggle() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WalkieToggle(false) { }
            WalkieToggle(true) { }
        }
    }
}