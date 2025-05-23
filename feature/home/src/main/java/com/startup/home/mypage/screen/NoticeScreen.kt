package com.startup.home.mypage.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.domain.model.notice.Notice
import com.startup.home.R
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.home.mypage.NoticeScreenNavigationEvent
import com.startup.home.mypage.NoticeViewState
import com.startup.webview.WalkieWebConstants
import com.startup.webview.WebViewWithBackHandling
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NoticeScreen(
    noticeViewState: NoticeViewState,
    onNavigationEvent: (NoticeScreenNavigationEvent) -> Unit
) {
    val noticeList by noticeViewState.noticeList.collectAsState()

    WebViewWithBackHandling(
        url = WalkieWebConstants.NOTICE_URL,
        title = stringResource(R.string.notice_title),
        onNavigationEvent = onNavigationEvent,
        backEvent = NoticeScreenNavigationEvent.Back
    )
}

/*
 추후 2차 스프린트에 활용 예정
 */
@Composable
private fun NoticeListItem(
    item: Notice = Notice(
        title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
        date = "2025년 1월 1일",
        detail = "~~~"
    ),
    onClickItem: (Notice) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .noRippleClickable { onClickItem.invoke(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1F)) {
            Text(
                item.title,
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                item.date,
                style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray400),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = WalkieTheme.colors.gray300
        )
    }
}

@Preview
@Composable
private fun PreviewNoticeScreen() {
    WalkieTheme {
        NoticeScreen(
            object : NoticeViewState {
                override val noticeList: StateFlow<List<Notice>>
                    get() = MutableStateFlow(
                        listOf(
                            Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            ), Notice(
                                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                                date = "2025년 1월 1일",
                                detail = "~~~"
                            )
                        )
                    )
            }, {}
        )
    }
}