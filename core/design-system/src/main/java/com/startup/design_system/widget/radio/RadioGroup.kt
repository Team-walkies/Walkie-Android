package com.startup.design_system.widget.radio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

/**
 *
 * @param options 선택 가능한 옵션 목록
 * @param selectedOption 현재 선택된 옵션
 * @param onOptionSelected 옵션이 선택되었을 때 호출되는 콜백
 * @param modifier Modifier
 */
@Composable
fun <T> RadioGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    optionContent: @Composable (option: T, isSelected: Boolean, onClick: () -> Unit) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            optionContent(
                option,
                option == selectedOption
            ) { onOptionSelected(option) }
        }
    }
}

/**
 *
 * @param options 선택 가능한 옵션 목록
 * @param selectedOption 현재 선택된 옵션
 * @param onOptionSelected 옵션이 선택되었을 때 호출되는 콜백
 * @param modifier Modifier
 */
@Composable
fun TextRadioGroup(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = onOptionSelected,
        modifier = modifier
    ) { option, isSelected, onClick ->
        TextRadioButton(
            text = option,
            selected = isSelected,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRadioGroup() {
    WalkieTheme {
        var selectedReason by remember { mutableStateOf<String?>("서비스 이용 불편") }

        Column(modifier = Modifier.padding(16.dp)) {
            TextRadioGroup(
                options = listOf(
                    "서비스 이용 불편",
                    "더 나은 대안 발견",
                    "사용 빈도 낮음",
                    "개인정보 보호",
                    "기타"
                ),
                selectedOption = selectedReason,
                onOptionSelected = { selectedReason = it }
            )
        }
    }
}
