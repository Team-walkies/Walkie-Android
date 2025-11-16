package com.startup.design_system.widget.tooltip

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

enum class WalkieTooltipArrowPosition {
    TopStart, TopCenter, TopEnd,
    BottomStart, BottomCenter, BottomEnd
}

class WalkieTooltipShape(
    private val arrowWidth: Dp,
    private val arrowHeight: Dp,
    private val radius: Dp,
    private val arrowPosition: WalkieTooltipArrowPosition
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val arrowWidthPx = arrowWidth.toPx()
        val arrowHeightPx = arrowHeight.toPx()

        val path = Path().apply {

            val arrowX = when (arrowPosition) {
                WalkieTooltipArrowPosition.TopStart, WalkieTooltipArrowPosition.BottomStart -> size.width * 0.15f
                WalkieTooltipArrowPosition.TopCenter, WalkieTooltipArrowPosition.BottomCenter -> size.width / 2
                WalkieTooltipArrowPosition.TopEnd, WalkieTooltipArrowPosition.BottomEnd -> size.width * 0.85f
            }

            when (arrowPosition) {
                WalkieTooltipArrowPosition.TopStart, WalkieTooltipArrowPosition.TopCenter, WalkieTooltipArrowPosition.TopEnd -> {
                    val tipY = arrowHeightPx / 4f

                    moveTo(arrowX - arrowWidthPx / 2f, arrowHeightPx)
                    lineTo(arrowX - arrowWidthPx * 0.15f, tipY)
                    quadraticTo(
                        arrowX,
                        0f,
                        arrowX + arrowWidthPx * 0.15f,
                        tipY
                    )
                    lineTo(arrowX + arrowWidthPx / 2f, arrowHeightPx)
                    close()

                    addRoundRect(
                        RoundRect(
                            rect = Rect(0f, arrowHeightPx, size.width, size.height),
                            cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
                        )
                    )
                }
                WalkieTooltipArrowPosition.BottomStart, WalkieTooltipArrowPosition.BottomCenter, WalkieTooltipArrowPosition.BottomEnd -> {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(0f, 0f, size.width, size.height - arrowHeightPx),
                            cornerRadius = CornerRadius(radius.toPx(), radius.toPx())
                        )
                    )

                    val leftX = arrowX - arrowWidthPx / 2f
                    val rightX = arrowX + arrowWidthPx / 2f
                    val bottomY = size.height
                    val topY = size.height - arrowHeightPx
                    val tipY = size.height - arrowHeightPx / 4f

                    moveTo(leftX, topY)
                    lineTo(arrowX - arrowWidthPx * 0.15f, tipY)
                    quadraticTo(
                        arrowX,
                        bottomY,
                        arrowX + arrowWidthPx * 0.15f,
                        tipY
                    )
                    lineTo(rightX, topY)
                    close()
                }
            }
        }

        Outline.Generic(path)
    }
}