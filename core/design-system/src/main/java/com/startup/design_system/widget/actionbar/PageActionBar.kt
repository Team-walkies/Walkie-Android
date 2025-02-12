package com.startup.design_system.widget.actionbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

@Composable
fun PageActionBar(pageActionBarType: PageActionBarType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = WalkieTheme.colors.white),
        contentAlignment = Alignment.CenterStart
    ) {
        when (pageActionBarType) {
            is PageActionBarType.JustBackActionBarType -> {}
            is PageActionBarType.TitleActionBarType -> {
                Text(
                    text = pageActionBarType.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
            }

            is PageActionBarType.TitleAndOptionalRightActionBarType -> {
                Text(
                    text = pageActionBarType.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd)
                        .noRippleClickable {
                            if (pageActionBarType.enabled) {
                                pageActionBarType.rightActionClicked.invoke()
                            }
                        },
                    text = pageActionBarType.rightActionTitle,
                    textAlign = TextAlign.End,
                    style = WalkieTheme.typography.head5.copy(color = if (pageActionBarType.enabled) pageActionBarType.enabledTextColor else pageActionBarType.disableTextColor)
                )
            }

            is PageActionBarType.TitleAndRightActionBarType -> {
                Text(
                    text = pageActionBarType.title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = WalkieTheme.typography.head6.copy(color = WalkieTheme.colors.gray700)
                )
                Text(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .align(Alignment.CenterEnd)
                        .noRippleClickable {
                            pageActionBarType.rightActionClicked.invoke()
                        },
                    text = pageActionBarType.rightActionTitle,
                    textAlign = TextAlign.End,
                    style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray400)
                )
            }
        }
        Icon(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
                .noRippleClickable { pageActionBarType.onBackClicked },
            painter = painterResource(R.drawable.ic_back),
            tint = WalkieTheme.colors.gray700,
            contentDescription = null
        )
    }
}

@Composable
@Preview
fun PreviewPageActionBar() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            PageActionBar(PageActionBarType.JustBackActionBarType({}))
            PageActionBar(PageActionBarType.TitleActionBarType({}, "제목"))
            PageActionBar(
                PageActionBarType.TitleAndOptionalRightActionBarType(
                    {},
                    "제목",
                    "버튼",
                    true,
                    WalkieTheme.colors.blue400,
                    WalkieTheme.colors.gray400,
                    {})
            )
            PageActionBar(
                PageActionBarType.TitleAndOptionalRightActionBarType(
                    {},
                    "제목",
                    "버튼",
                    false,
                    WalkieTheme.colors.blue400,
                    WalkieTheme.colors.gray400,
                    {})
            )
            PageActionBar(PageActionBarType.TitleAndRightActionBarType({}, "제목", "버튼", {}))
        }
    }
}