package com.startup.home.spot.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.home.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BottomSheetReviewOption(
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    onClickCancel: () -> Unit,
    sheetState: SheetState
) {
    WalkieTheme {
        ModalBottomSheet(
            onDismissRequest = { onClickCancel() },
            sheetState = sheetState,
            tonalElevation = 24.dp,
            containerColor = WalkieTheme.colors.white,
            dragHandle = {
                WalkieDragHandle()
            },
            contentColor = WalkieTheme.colors.white,
            scrimColor = WalkieTheme.colors.blackOpacity60,
        ) {
            ReviewOptionContent(onClickDelete, onClickModify)
        }
    }
}

@Composable
private fun ReviewOptionContent(
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = WalkieTheme.colors.white)
            .padding(
                start = 16.dp,
                top = 12.dp,
                end = 16.dp,
                bottom = 28.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp)
                .weight(1F)
                .noRippleClickable { onClickDelete.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                tint = WalkieTheme.colors.red100,
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.calendar_review_delete)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.calendar_review_delete),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.red100)
            )
        }
        Column(
            modifier = Modifier
                .background(color = WalkieTheme.colors.gray50, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp)
                .weight(1F)
                .noRippleClickable { onClickModify.invoke() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                tint = WalkieTheme.colors.gray500,
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = stringResource(R.string.calendar_review_modify)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.calendar_review_modify),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewReviewOptionContent() {
    WalkieTheme {
        ReviewOptionContent({},{})
    }
}