package com.startup.home.mypage.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.design_system.widget.button.DangerButton
import com.startup.design_system.widget.checkbox.TextCheckBox

private const val WALKIE_EMAIL = "walkieofficial@gmail.com"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlinkBottomSheet(
    onDismiss: () -> Unit,
    onConfirmUnlink: () -> Unit,
    sheetState: SheetState
) {
    val context = LocalContext.current

    WalkieTheme {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            tonalElevation = 24.dp,
            containerColor = WalkieTheme.colors.white,
            dragHandle = { WalkieDragHandle() },
            contentColor = WalkieTheme.colors.white,
            scrimColor = WalkieTheme.colors.blackOpacity60,
        ) {
            UnlinkBottomSheetContent(
                onConfirmUnlink = onConfirmUnlink,
                onCopyEmail = {
                    copyToClipboard(context, WALKIE_EMAIL)
                }
            )
        }
    }
}

@Composable
private fun UnlinkBottomSheetContent(
    onConfirmUnlink: () -> Unit,
    onCopyEmail: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = WalkieTheme.colors.white)
            .padding(horizontal = 16.dp)
            .padding(bottom = 28.dp, top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_title),
            style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_info_1_title),
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
                Text(
                    text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_info_1_desc),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.noRippleClickable { onCopyEmail() },
                        text = WALKIE_EMAIL,
                        style = WalkieTheme.typography.body2.copy(
                            color = WalkieTheme.colors.gray500,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    Text(
                        text = stringResource(com.startup.resource.R.string.copy),
                        style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray500),
                        modifier = Modifier
                            .background(
                                color = WalkieTheme.colors.gray200,
                                shape = RoundedCornerShape(99.dp)
                            )
                            .noRippleClickable { onCopyEmail() }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.gray50,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_info_2_title),
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
                Text(
                    text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_info_2_desc),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
            }
        }

        TextCheckBox(
            text = stringResource(com.startup.resource.R.string.unlink_bottom_sheet_agree),
            checked = isChecked,
            onCheckedChanged = { isChecked = it }
        )

        DangerButton(
            text = stringResource(com.startup.resource.R.string.unlink),
            enabled = isChecked,
            modifier = Modifier.fillMaxWidth()
        ) {
            onConfirmUnlink()
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("email", text)
    clipboard.setPrimaryClip(clip)
}

@Preview(showBackground = true)
@Composable
private fun PreviewUnlinkBottomSheetContent() {
    WalkieTheme {
        UnlinkBottomSheetContent(
            onConfirmUnlink = {},
            onCopyEmail = {}
        )
    }
}
