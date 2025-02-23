package com.startup.design_system.widget.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

@Composable
fun SnackBarHost(snackBarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = remember { snackBarHostState },
    ) { snackBarData ->
        val customVisuals = snackBarData.visuals as? SnackBarCustomVisuals
        if (customVisuals != null) {
            SnackBarContent(visuals = customVisuals)
        } else {
            Snackbar(snackbarData = snackBarData)
        }
    }
}

@Composable
fun SnackBarContent(visuals: SnackBarCustomVisuals) {
    Row(
        modifier = Modifier
            .width(343.dp)
            .height(56.dp)
            .background(
                color = WalkieTheme.colors.gray900Opacity70,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = visuals.annotatedMessage,
            modifier = Modifier
                .padding(vertical = 18.dp)
                .padding(end = 10.dp)
                .weight(1F, false),
            maxLines = 1,
            style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.white),
            overflow = TextOverflow.Ellipsis,
        )
        visuals.actionLabel?.let { label ->
            Text(
                modifier = Modifier
                    .noRippleClickable {
                        visuals.action?.invoke()
                    }
                    .wrapContentWidth()
                    .background(
                        color = visuals.actionLabelColor ?: WalkieTheme.colors.blue300,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                text = label,
                maxLines = 1,
                style = WalkieTheme.typography.caption1,
                color = WalkieTheme.colors.white
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSnackBar() {
    WalkieTheme {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            SnackBarContent(
                SnackBarCustomVisuals(
                    annotatedMessage = AnnotatedString(
                        "스타일 적용된 메시지",
                        spanStyles = listOf(
                            AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), 0, 5)
                        )
                    ),
                    actionLabelColor = WalkieTheme.colors.gray900,
                    actionLabel = "중단하기",
                    action = {}
                )
            )
            SnackBarContent(
                SnackBarCustomVisuals(
                    annotatedMessage = AnnotatedString(
                        "스타일 적용된 메시지",
                        spanStyles = listOf(
                            AnnotatedString.Range(SpanStyle(fontWeight = FontWeight.Bold), 0, 5)
                        )
                    )
                )
            )
        }
    }
}