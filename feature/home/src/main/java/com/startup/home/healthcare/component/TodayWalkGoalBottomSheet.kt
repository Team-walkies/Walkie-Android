package com.startup.home.healthcare.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.extension.formatWithLocale
import com.startup.common.extension.noRippleClickable
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayWalkGoalBottomSheet(
    currentGoal: Int,
    onSelectGoal: (Int) -> Unit,
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
            TodayWalkGoalBottomSheetContent(
                currentGoal = currentGoal,
                onSelectGoal = onSelectGoal,
            )
        }
    }
}

@Composable
private fun TodayWalkGoalBottomSheetContent(
    currentGoal: Int, onSelectGoal: (Int) -> Unit,
) {
    var selectedGoal by rememberSaveable { mutableIntStateOf(currentGoal.takeIf { it > 0 } ?: 6_000) }
    Column(
        modifier = Modifier
            .background(color = WalkieTheme.colors.white)
            .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 28.dp)
    ) {
        Text(
            text = stringResource(R.string.healthcare_today_walk_goal_title),
            style = WalkieTheme.typography.head4.copy(color = WalkieTheme.colors.gray700)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (goal in 4_000..10_000 step 2_000) {
                val isSelected = goal == selectedGoal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = WalkieTheme.colors.gray50, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 14.dp, horizontal = 16.dp)
                        .noRippleClickable {
                            selectedGoal = goal
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = goal.formatWithLocale(), style = WalkieTheme.typography.body1.copy(
                            color = if (isSelected) WalkieTheme.colors.blue400 else WalkieTheme.colors.gray700
                        )
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .background(color = WalkieTheme.colors.white)
                                .size(20.dp)
                                .clip(shape = CircleShape)
                                .border(width = 5.dp, color = WalkieTheme.colors.blue300, shape = CircleShape)
                        ) {}
                    } else {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(shape = CircleShape)
                                .background(color = WalkieTheme.colors.gray200)
                        ) {}
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSelectGoal.invoke(selectedGoal) },
            enabled = selectedGoal != currentGoal,
            text = stringResource(R.string.healthcare_today_walk_goal_select_btn_text)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewTodayWalkGoalBottomSheet() {
    WalkieTheme {
        TodayWalkGoalBottomSheetContent(
            currentGoal = 6_000,
            onSelectGoal = {},
        )
    }
}