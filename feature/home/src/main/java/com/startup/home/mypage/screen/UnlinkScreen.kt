package com.startup.home.mypage.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.radio.TextRadioButton
import com.startup.design_system.widget.textfield.WalkieTextArea
import com.startup.home.R
import com.startup.home.mypage.UnlinkUiEvent
import com.startup.home.mypage.component.UnlinkBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlinkScreen(
    userNickName: String,
    onNavigationEvent: (NavigationEvent) -> Unit,
    uiEventSender: (UnlinkUiEvent) -> Unit
) {
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var otherReasonText by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val unlinkReasons = listOf(
        stringResource(R.string.unlink_reason_lack_of_fun),
        stringResource(R.string.unlink_reason_battery_drain),
        stringResource(R.string.unlink_reason_inconvenient),
        stringResource(R.string.unlink_reason_etc)
    )

    val etcReason = stringResource(id = R.string.unlink_reason_etc)

    val isNextButtonEnabled = selectedReason != null &&
            (selectedReason != etcReason || otherReasonText.isNotBlank())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.TitleAndOptionalRightActionBarType(
                onBackClicked = { onNavigationEvent.invoke(NavigationEvent.Back) },
                title = stringResource(R.string.unlink_header_title),
                rightActionTitle = stringResource(id = R.string.unlink_next),
                enabled = isNextButtonEnabled,
                enabledTextColor = WalkieTheme.colors.blue300,
                disableTextColor = WalkieTheme.colors.gray400,
                rightActionClicked = {
                    showBottomSheet = true
                }
            )
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.unlink_title, userNickName),
                style = WalkieTheme.typography.head3.copy(color = WalkieTheme.colors.gray700)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.unlink_sub_title),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray400)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(id = R.string.unlink_reason_select_label),
                style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray700)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                unlinkReasons.forEach { reason ->
                    TextRadioButton(
                        text = reason,
                        selected = selectedReason == reason,
                        onClick = { selectedReason = reason }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = selectedReason == etcReason) {
                WalkieTextArea(
                    value = otherReasonText,
                    onValueChange = { otherReasonText = it },
                    placeholder = stringResource(id = R.string.request_opinion_placeholder),
                    maxLength = 250,
                    height = 172.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // 바텀시트 표시
    if (showBottomSheet) {
        UnlinkBottomSheet(
            onDismiss = {
                scope.launch {
                    bottomSheetState.hide()
                    showBottomSheet = false
                }
            },
            onConfirmUnlink = {
                scope.launch {
                    bottomSheetState.hide()
                    showBottomSheet = false
                    uiEventSender.invoke(UnlinkUiEvent.UnlinkWalkie)
                }
            },
            sheetState = bottomSheetState
        )
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewUnlinkScreen() {
    WalkieTheme {
        UnlinkScreen("승빈짱짱", {}, {})
    }
}