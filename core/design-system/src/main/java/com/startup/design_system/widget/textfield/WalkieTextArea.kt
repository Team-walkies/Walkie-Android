package com.startup.design_system.widget.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.Container
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme

/**
 * 워키 공통 TextField
 *
 * TextField 높이 258dp 고정, 입력 내용이 넘칠 경우 내부 스크롤
 * 텍스트 입력 영역 내부 패딩: 16dp
 *
 * @param value 입력된 텍스트 값
 * @param onValueChange 텍스트 변경 시 호출되는 콜백
 * @param modifier Modifier
 * @param placeholder 플레이스홀더 텍스트
 * @param enabled 활성화 여부
 * @param isError 에러 상태 여부
 * @param supportingText 하단 서포팅 텍스트 (에러 메시지 등)
 * @param maxLength 최대 글자수 (null이면 글자수 표시 안함)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkieTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    maxLength: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(258.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    if (maxLength == null) {
                        onValueChange(newValue)
                    } else {
                        if (newValue.length <= maxLength) {
                            onValueChange(newValue)
                        } else {
                            onValueChange(newValue.take(maxLength))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(258.dp),
                enabled = enabled,
                textStyle = WalkieTheme.typography.body2.copy(
                    color = WalkieTheme.colors.gray900
                ),
                interactionSource = interactionSource,
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = value,
                        visualTransformation = VisualTransformation.None,
                        innerTextField = innerTextField,
                        placeholder = {
                            if (placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = WalkieTheme.typography.body2,
                                    color = WalkieTheme.colors.gray400
                                )
                            }
                        },
                        enabled = enabled,
                        singleLine = false,
                        interactionSource = interactionSource,
                        isError = isError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = WalkieTheme.colors.blue300,
                            unfocusedBorderColor = WalkieTheme.colors.gray300,
                            errorBorderColor = WalkieTheme.colors.red100,
                            disabledBorderColor = WalkieTheme.colors.gray200,
                            focusedContainerColor = WalkieTheme.colors.gray100,
                            unfocusedContainerColor = WalkieTheme.colors.gray100,
                            errorContainerColor = WalkieTheme.colors.gray100,
                            disabledContainerColor = WalkieTheme.colors.gray100,
                        ),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = if (maxLength != null) 32.dp else 16.dp
                        ),
                        container = {
                            Container(
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = WalkieTheme.colors.blue300,
                                    unfocusedBorderColor = WalkieTheme.colors.gray300,
                                    errorBorderColor = WalkieTheme.colors.red100,
                                    disabledBorderColor = WalkieTheme.colors.gray200,
                                    focusedContainerColor = WalkieTheme.colors.gray100,
                                    unfocusedContainerColor = WalkieTheme.colors.gray100,
                                    errorContainerColor = WalkieTheme.colors.gray100,
                                    disabledContainerColor = WalkieTheme.colors.gray100,
                                ),
                                shape = RoundedCornerShape(20.dp),
                                focusedBorderThickness = 1.dp,
                                unfocusedBorderThickness = 1.dp
                            )
                        }
                    )
                }
            )

            if (maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    textAlign = TextAlign.End,
                    style = WalkieTheme.typography.body2,
                    color = WalkieTheme.colors.gray400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }

        if (supportingText != null) {
            Text(
                text = supportingText,
                style = WalkieTheme.typography.caption1,
                color = if (isError) WalkieTheme.colors.red100 else WalkieTheme.colors.gray500
            )
        }
    }
}

@Composable
@Preview
fun PreviewWalkieTextArea() {
    WalkieTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            val (value1, setValue1) = remember { mutableStateOf("") }
            WalkieTextArea(
                value = value1,
                onValueChange = setValue1,
                placeholder = "탈퇴 사유를 입력해주세요"
            )

            val (value2, setValue2) = remember { mutableStateOf("입력된 내용이 있는 상태입니다.\n여러 줄의 텍스트를 입력할 수 있습니다.") }
            WalkieTextArea(
                value = value2,
                onValueChange = setValue2,
                placeholder = "탈퇴 사유를 입력해주세요",
                maxLength = 500
            )

            val (value3, setValue3) = remember { mutableStateOf("에러 상태입니다.") }
            WalkieTextArea(
                value = value3,
                onValueChange = setValue3,
                isError = true,
                supportingText = "탈퇴 사유를 입력해주세요",
                maxLength = 200
            )

            val (value4, setValue4) = remember { mutableStateOf("") }
            WalkieTextArea(
                value = value4,
                onValueChange = setValue4,
                placeholder = "최대 1000자까지 입력 가능",
                maxLength = 1000
            )
        }
    }
}
