package com.startup.home.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.startup.common.extension.shimmerEffect
import com.startup.common.extension.shimmerEffectGray200

@Composable
internal fun SkeletonCharacterComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(188.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .shimmerEffect()
            .padding(bottom = 16.dp, top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(shape = CircleShape)
                .shimmerEffectGray200()
        ) {}
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(horizontal = 30.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffectGray200()
        ) {}
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(horizontal = 60.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffectGray200()
        ) {}
    }
}