package com.startup.login.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.extension.ofMaxLength
import com.startup.login.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.login.login.model.NickNameSettingEvent
import com.startup.login.login.model.NickNameViewState
import com.startup.login.login.model.NickNameViewStateImpl
import com.startup.common.util.hasSpecialCharacters
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun NickNameSettingScreen(
    viewState: NickNameViewState,
    viewEvent: (NickNameSettingEvent) -> Unit
) {
    val enabledTextColor = WalkieTheme.colors.blue400
    val disableTextColor = WalkieTheme.colors.gray400
    val placeHolder by viewState.placeHolder.collectAsStateWithLifecycle()
    val nickName by viewState.nickName.collectAsStateWithLifecycle()
    val nickNameContainedSpecialCharacters by remember { derivedStateOf { nickName.text.hasSpecialCharacters() } }
    val enabledSave by remember { derivedStateOf { nickName.text.isNotBlank() && !nickNameContainedSpecialCharacters } }
    val underLineColor =
        if (nickNameContainedSpecialCharacters) WalkieTheme.colors.red100 else if (enabledSave) WalkieTheme.colors.blue300 else WalkieTheme.colors.gray200
    val maxLength = 10
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)
    ) {
        // Start PageActionBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(color = WalkieTheme.colors.white),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterEnd)
                    .noRippleClickable {
                        if (enabledSave) {
                            viewEvent.invoke(NickNameSettingEvent.OnClickNickNameConfirm(nickName = nickName.text))
                        }
                    },
                text = stringResource(R.string.onboarding_nick_name_apply),
                textAlign = TextAlign.End,
                style = WalkieTheme.typography.head5.copy(color = if (enabledSave) enabledTextColor else disableTextColor)
            )
        }
        // End PageActionBar
        Spacer(modifier = Modifier.height(44.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.onboarding_nick_name_title),
                style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
            )
            Spacer(modifier = Modifier.height(38.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = WalkieTheme.colors.white),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = nickName,
                    textStyle = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700),
                    onValueChange = { value ->
                        value.ofMaxLength(maxLength).also { convertTextValue ->
                            viewEvent.invoke(
                                NickNameSettingEvent.OnNickNameChanged(
                                    convertTextValue
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1F),

                    singleLine = true,
                    maxLines = 1,
                    decorationBox = { textField ->
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(24.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (nickName.text.isBlank()) {
                                        Text(
                                            modifier = Modifier
                                                .height(24.dp),
                                            text = placeHolder,
                                            style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray500)
                                        )
                                    }
                                    textField()
                                }
                                if (nickName.text.isNotBlank()) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_text_delete),
                                        contentDescription = null,
                                        tint = WalkieTheme.colors.gray400,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .noRippleClickable {
                                                viewEvent.invoke(
                                                    NickNameSettingEvent.OnNickNameChanged(
                                                        TextFieldValue("")
                                                    )
                                                )
                                            },
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 6.dp),
                                thickness = 2.dp,
                                color = underLineColor
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${nickName.text.length}/$maxLength",
                                    style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400)
                                )
                                if (nickNameContainedSpecialCharacters) {
                                    Text(
                                        text = stringResource(R.string.warning_nick_name_has_special_character),
                                        style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.red100)
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewNickNameSettingScreen() {
    WalkieTheme {
        NickNameSettingScreen(NickNameViewStateImpl(placeHolder = MutableStateFlow("")), {})
    }
}