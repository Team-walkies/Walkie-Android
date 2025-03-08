package com.startup.home.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.home.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

/**
 * 알림 화면 상태 클래스
 */
private data class NotificationListScreenState(
    val isDeleteMode: Boolean,
    val notifications: List<NotificationListItemModel>,
    val setDeleteMode: (Boolean) -> Unit,
    val deleteNotification: (Int) -> Unit
)

@Composable
fun NotificationListScreen(
    onBackPressed: () -> Unit
) {
    // 상태 정의
    val screenState = rememberNotificationListScreenState()

    Scaffold(
        topBar = {
            PageActionBar(PageActionBarType.JustBackActionBarType {
                if (screenState.isDeleteMode) {
                    screenState.setDeleteMode(false)
                } else {
                    onBackPressed()
                }
            })
        },
        containerColor = WalkieTheme.colors.white
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = WalkieTheme.colors.white)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            // 상단 액션 바
            NotificationActionBar(
                isDeleteMode = screenState.isDeleteMode,
                onToggleDeleteMode = { screenState.setDeleteMode(!screenState.isDeleteMode) }
            )

            // 알림 목록
            NotificationList(
                notifications = screenState.notifications,
                isDeleteMode = screenState.isDeleteMode,
                onDeleteClick = screenState.deleteNotification
            )
        }
    }
}

/**
 * 알림 화면 상태 관리를 위한 클래스
 */
@Composable
private fun rememberNotificationListScreenState(): NotificationListScreenState {
    var isDeleteMode by remember { mutableStateOf(false) }
    var notifications by remember {
        mutableStateOf(getDefaultNotificationList())
    }

    return remember(isDeleteMode, notifications) {
        NotificationListScreenState(
            isDeleteMode = isDeleteMode,
            notifications = notifications,
            setDeleteMode = { isDeleteMode = it },
            deleteNotification = { itemId ->
                notifications = notifications.filter { it.id != itemId }
            }
        )
    }
}

/**
 * 삭제/취소 액션 바
 */
@Composable
private fun NotificationActionBar(
    isDeleteMode: Boolean,
    onToggleDeleteMode: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(color = WalkieTheme.colors.gray50),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .width(45.dp)
                .padding(end = 16.dp)
                .noRippleClickable(onClick = onToggleDeleteMode),
            text = if (isDeleteMode) {
                stringResource(R.string.cancel)
            } else {
                stringResource(R.string.delete)
            },
            style = WalkieTheme.typography.body2,
            color = WalkieTheme.colors.gray500,
        )
    }
}

/**
 * 알림 목록 컴포넌트
 */
@Composable
private fun NotificationList(
    notifications: List<NotificationListItemModel>,
    isDeleteMode: Boolean,
    onDeleteClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notifications) { item ->
            NotificationListItem(
                modifier = Modifier,
                model = item,
                isDeleteMode = isDeleteMode,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

private fun getDefaultNotificationList(): List<NotificationListItemModel> {
    return listOf(
        NotificationListItemModel(
            id = 1,
            imageType = NotificationImageType.EGG,
            title = "스팟 도착",
            content = "짝짝, 여의도한강공원에 도착했어요. 앱에 접속해 알을 열어보세요. 한번 더 열어 보세요",
            timeLapse = "31분 전"
        ),
        NotificationListItemModel(
            id = 2,
            imageType = NotificationImageType.WALK,
            title = "새 메시지",
            content = "친구가 새 메시지를 보냈습니다. 지금 확인해보세요.",
            timeLapse = "1시간 전"
        ),
        NotificationListItemModel(
            id = 3,
            imageType = NotificationImageType.SPOT,
            title = "새로운 장소",
            content = "주변에 새로운 장소가 등록되었습니다. 확인해보세요. ",
            timeLapse = "2시간 전"
        )
    )
}