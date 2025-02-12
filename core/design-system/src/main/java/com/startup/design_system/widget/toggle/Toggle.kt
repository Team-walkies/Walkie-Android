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
fun Toggle(checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    val disabledColor = WalkieTheme.colors.gray300
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
            disabledUncheckedBorderColor = disabledColor,
            disabledCheckedIconColor = disabledColor,
            disabledCheckedThumbColor = disabledColor,
            disabledCheckedTrackColor = disabledColor,
            disabledCheckedBorderColor = disabledColor,
            disabledUncheckedIconColor = disabledColor,
            disabledUncheckedThumbColor = disabledColor,
            disabledUncheckedTrackColor = disabledColor
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewToggle() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Toggle(false) { }
            Toggle(true) { }
        }
    }
}