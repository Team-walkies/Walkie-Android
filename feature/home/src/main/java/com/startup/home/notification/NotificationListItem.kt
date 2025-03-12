package com.startup.home.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.home.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable


@Composable
fun NotificationListItem(
    modifier: Modifier = Modifier,
    model: NotificationListItemModel,
    isDeleteMode: Boolean,
    onDeleteClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 이미지
            Image(
                painter = painterResource(id = model.imageType.resId),
                contentDescription = null,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row {
                    Text(
                        text = model.title,
                        style = WalkieTheme.typography.head6,
                        maxLines = 1,
                        color = WalkieTheme.colors.gray700,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (isDeleteMode) {
                        // X 아이콘 (삭제 버튼)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(20.dp)
                                .noRippleClickable { onDeleteClick(model.id) },
                            tint = WalkieTheme.colors.gray500
                        )
                    } else {
                        // 시간 정보
                        Text(
                            text = model.timeLapse,
                            style = WalkieTheme.typography.caption1,
                            color = WalkieTheme.colors.gray400
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = model.content,
                    style = WalkieTheme.typography.body2,
                    color = WalkieTheme.colors.gray500,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationListItemPreview() {
    val sampleNotification = NotificationListItemModel(
        id = 1,
        imageType = NotificationImageType.EGG,
        title = "스팟 도착",
        content = "짝짝, 여의도한강공원에 도착했어요. 앱에 접속해 알을 열어보세요.",
        timeLapse = "31분 전"
    )

    WalkieTheme {
        Column {
            NotificationListItem(
                modifier = Modifier,
                model = sampleNotification,
                isDeleteMode = true
            ) {}
            NotificationListItem(
                modifier = Modifier,
                model = sampleNotification,
                isDeleteMode = false
            ) {}
        }
    }
}