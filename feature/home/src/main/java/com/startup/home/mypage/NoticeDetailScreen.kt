package com.startup.home.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.domain.model.notice.Notice
import com.startup.design_system.ui.WalkieTheme

@Composable
fun NoticeDetailScreen(notice: Notice, onNavigationEvent: (NavigationEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
    ) {
        PageActionBar(
            PageActionBarType.JustBackActionBarType { onNavigationEvent.invoke(NavigationEvent.Back) }
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = notice.date,
                style = WalkieTheme.typography.caption1.copy(color = WalkieTheme.colors.gray400)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = notice.title,
                style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray700)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 4.dp,
                color = WalkieTheme.colors.gray50
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = notice.detail,
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray700)
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun PreviewNoticeDetailScreen() {
    WalkieTheme {
        NoticeDetailScreen(
            Notice(
                title = "신년을 맞아 새로운 캐릭터가 추가되었어요!",
                date = "2025년 1월 1일",
                detail = "안녕하세요.\n" +
                        "워키 팀입니다.\n" +
                        "\n" +
                        "2025년 12월 31일부터 스팟 방문 정책이 변경됩니다. 한 번 방문한 스팟은 3일간의 제한 기간이 적용된 후 다시 방문할 수 있습니다. \n" +
                        "서비스 이용에 참고해 주시기 바랍니다.\n" +
                        "\n" +
                        "■ 적용 일정\n" +
                        "2025년 1월 1일 (수요일)\n" +
                        "\n" +
                        "■ 변경 내용\n" +
                        "기존: 한 번 방문한 스팟도 매일 방문 가능\n" +
                        "변경: 한 번 방문한 스팟은 3일간의 제한 기간 후 방문 가능.\n" +
                        "\n" +
                        "워키 팀은 앞으로도 더 나은 서비스를 제공해드릴 수 있도록 최선을 다하겠습니다.\n" +
                        "감사합니다.\n" +
                        "\n" +
                        "워키 팀 드림"
            ), {}
        )
    }
}