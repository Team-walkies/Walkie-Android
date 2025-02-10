package com.startup.design_system.widget.checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme

@Composable
fun WalkieCheckBox(checked: Boolean, onCheckedChanged: (Boolean) -> Unit) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChanged,
        modifier = Modifier
            .size(28.dp)
            .padding(3.5.dp),
        colors = CheckboxColors(
            checkedBoxColor = WalkieTheme.colors.blue200,
            checkedBorderColor = WalkieTheme.colors.blue200,
            checkedCheckmarkColor = WalkieTheme.colors.white,
            uncheckedBoxColor = WalkieTheme.colors.gray200,
            uncheckedBorderColor = WalkieTheme.colors.gray200,
            uncheckedCheckmarkColor = WalkieTheme.colors.gray200,
            /*이 아래는 의미 없긴함..*/
            disabledCheckedBoxColor = WalkieTheme.colors.gray200,
            disabledBorderColor = WalkieTheme.colors.gray200,
            disabledUncheckedBoxColor = WalkieTheme.colors.gray200,
            disabledIndeterminateBoxColor = WalkieTheme.colors.gray200,
            disabledIndeterminateBorderColor = WalkieTheme.colors.gray200,
            disabledUncheckedBorderColor = WalkieTheme.colors.gray200
        )
    )
}

@Preview
@Composable
fun PreviewWalkieCheckBox() {
    WalkieTheme {
        Column {
            WalkieCheckBox(checked = true) {

            }
            WalkieCheckBox(checked = false) {

            }
        }
    }
}