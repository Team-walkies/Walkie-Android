package com.startup.design_system.widget.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.startup.ui.WalkieTheme

@Composable
fun WalkieDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
            .width(44.dp)
            .height(4.dp)
            .background(color = WalkieTheme.colors.gray200, RoundedCornerShape(4.dp))
    )
}