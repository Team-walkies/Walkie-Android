package com.startup.design_system.widget.checkbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

@Composable
fun WalkieCheckBox(checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    val disabledColor = WalkieTheme.colors.gray300
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChanged,
        modifier = Modifier
            .size(28.dp)
            .padding(3.5.dp),
        colors = CheckboxColors(
            checkedBoxColor = WalkieTheme.colors.blue300,
            checkedBorderColor = WalkieTheme.colors.blue300,
            checkedCheckmarkColor = WalkieTheme.colors.white,
            uncheckedBoxColor = WalkieTheme.colors.gray200,
            uncheckedBorderColor = WalkieTheme.colors.gray200,
            uncheckedCheckmarkColor = WalkieTheme.colors.gray200,
            disabledCheckedBoxColor = disabledColor,
            disabledBorderColor = disabledColor,
            disabledUncheckedBoxColor = disabledColor,
            disabledIndeterminateBoxColor = disabledColor,
            disabledIndeterminateBorderColor = disabledColor,
            disabledUncheckedBorderColor = disabledColor
        )
    )
}

@Preview
@Composable
fun PreviewWalkieCheckBox() {
    WalkieTheme {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            WalkieCheckBox(checked = true) {

            }
            WalkieCheckBox(checked = false) {

            }
        }
    }
}