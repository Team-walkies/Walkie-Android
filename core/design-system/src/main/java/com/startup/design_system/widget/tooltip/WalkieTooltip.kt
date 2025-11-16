package com.startup.design_system.widget.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.startup.design_system.ui.WalkieTheme


private val TOOLTIP_MAX_WIDTH = 300.dp
private val TOOLTIP_MIN_HEIGHT = 30.dp
private val ARROW_HEIGHT = 8.dp

/**
 * @param text 툴팁에 표시할 텍스트
 * @param anchorPosition 툴팁이 표시될 위치
 * @param onClose 툴팁 닫기 이벤트
 */
@Composable
fun WalkieTooltip(
    modifier: Modifier = Modifier,
    text: String,
    anchorPosition: WalkieTooltipArrowPosition,
    onClose: () -> Unit,
    anchorOffset: IntOffset,
    onTooltipSizeCalculated: (IntSize) -> Unit,
) {
    Box(
        modifier = modifier
            .offset { anchorOffset }
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = TOOLTIP_MAX_WIDTH)
                .heightIn(min = TOOLTIP_MIN_HEIGHT + ARROW_HEIGHT)
                .onGloballyPositioned { onTooltipSizeCalculated(it.size) }
                .background(
                    color = WalkieTheme.colors.gray600,
                    shape = WalkieTooltipShape(
                        arrowPosition = anchorPosition,
                        arrowWidth = 10.dp,
                        arrowHeight = ARROW_HEIGHT,
                        radius = 8.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .offset(
                    y = when (anchorPosition) {
                        WalkieTooltipArrowPosition.TopStart, WalkieTooltipArrowPosition.TopCenter, WalkieTooltipArrowPosition.TopEnd -> ARROW_HEIGHT / 2
                        WalkieTooltipArrowPosition.BottomStart, WalkieTooltipArrowPosition.BottomCenter, WalkieTooltipArrowPosition.BottomEnd -> -ARROW_HEIGHT / 2
                    }
                )
                .clickable { onClose() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier,
                text = text,
                maxLines = 2,
                style = WalkieTheme.typography.body2.copy(
                    color = WalkieTheme.colors.white,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlowTooltipPreview() {
    WalkieTheme {
        WalkieTooltip(
            text = "걸음 수를 채우면 알을 받아요",
            anchorPosition = WalkieTooltipArrowPosition.TopEnd,
            onClose = {},
            anchorOffset = IntOffset.Zero,
            onTooltipSizeCalculated = {}
        )
    }
}
