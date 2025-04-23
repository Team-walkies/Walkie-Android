package com.startup.home.egg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.extension.shimmerEffect
import com.startup.common.extension.shimmerEffectGray200
import com.startup.common.util.DateUtil
import com.startup.design_system.widget.progress.ProgressSmall
import com.startup.design_system.widget.tag.TagSmall
import com.startup.home.R
import com.startup.home.egg.model.MyEggModel
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.noRippleClickable
import com.startup.home.egg.model.EggKind
import java.text.NumberFormat
import java.util.Locale

@Composable
internal fun EggGridItemComponent(
    modifier: Modifier = Modifier,
    egg: MyEggModel,
    onClickEgg: (MyEggModel) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (egg.play) {
                    Modifier.border(
                        width = 2.dp,
                        color = WalkieTheme.colors.blue300,
                        shape = RoundedCornerShape(20.dp)
                    )
                } else Modifier
            )
            .background(
                color = WalkieTheme.colors.gray100,
                shape = RoundedCornerShape(20.dp)
            )
            .noRippleClickable {
                onClickEgg.invoke(egg)
            }
            .padding(bottom = 16.dp, top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (egg.play) {
                Icon(
                    painter = painterResource(R.drawable.ic_foot),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp),
                    contentDescription = stringResource(R.string.desc_egg_play_icon),
                    tint = WalkieTheme.colors.blue300
                )
            }
            Image(
                painter = painterResource(egg.eggKind.drawableResId),
                contentDescription = stringResource(R.string.desc_egg),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp)
                    .padding(top = 4.dp),
            )
        }
        TagSmall(
            text = stringResource(egg.eggKind.rankStrResId),
            textColor = egg.eggKind.getTextColor()
        )
        ProgressSmall(
            modifier = Modifier.padding(horizontal = 47.dp),
            egg.nowStep.toFloat() / egg.needStep.toFloat()
        )
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = kotlin.runCatching { formatter.format(egg.nowStep) }
                    .getOrElse { egg.nowStep.toString() },
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700)
            )
            Text(
                text = "/",
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray700)
            )
            Text(
                text = kotlin.runCatching { formatter.format(egg.needStep) }
                    .getOrElse { egg.needStep.toString() },
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray500)
            )
        }
    }
}

@Composable
internal fun SkeletonEggGridItemComponent() {
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
        Image(
            painter = painterResource(R.drawable.egg_empty),
            modifier = Modifier.size(80.dp),
            contentDescription = stringResource(R.string.desc_egg_empty)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(horizontal = 60.dp)
                .clip(shape = RoundedCornerShape(8.dp))
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
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEggGridItemComponent() {
    WalkieTheme {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            EggGridItemComponent(
                modifier = Modifier
                    .width(150.dp)
                    .height(188.dp),
                egg = MyEggModel(
                    eggId = 0,
                    nowStep = 8334,
                    needStep = 10000,
                    play = false,
                    characterId = -1,
                    eggKind = EggKind.Epic,
                    obtainedPosition = "대전시 유성구",
                    obtainedDate = DateUtil.convertDateTimeFormat("2024-01-12 12:20:10")
                )
            ) {}
            EggGridItemComponent(
                modifier = Modifier
                    .width(150.dp)
                    .height(188.dp),
                egg = MyEggModel(
                    eggId = 0,
                    nowStep = 8334,
                    needStep = 10000,
                    play = true,
                    characterId = -1,
                    eggKind = EggKind.Legend,
                    obtainedPosition = "대전시 유성구",
                    obtainedDate = DateUtil.convertDateTimeFormat("2024-01-12 12:20:10")
                )
            ) {}
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxHeight(),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(6) {
                    SkeletonEggGridItemComponent()
                }
            }
        }
    }
}