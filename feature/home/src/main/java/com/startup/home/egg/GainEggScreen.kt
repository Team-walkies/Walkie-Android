package com.startup.home.egg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.util.EMPTY_STRING
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.home.R
import com.startup.ui.WalkieTheme

@Composable
fun GainEggScreen(eggList: List<MyEggModel>) {
    Column(modifier = Modifier.fillMaxSize().background(WalkieTheme.colors.white)) {
        PageActionBar(
            PageActionBarType.TitleAndRightActionBarType(
                onBackClicked = {},
                title = EMPTY_STRING,
                rightActionTitle = stringResource(R.string.right_title_egg_info),
                rightActionClicked = {}
            )
        )
        val scrollState = rememberScrollableState { delta -> delta }
        Column(
            modifier = Modifier
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical
                )
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            Row {
                Text(
                    stringResource(R.string.current_gain_egg),
                    style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    eggList.size.toString(),
                    style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray500)
                )
            }
            if (eggList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stringResource(R.string.current_gain_egg_choice),
                    style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
                )
                LazyVerticalGrid(
                    modifier = Modifier.padding(top = 20.dp),
                    columns = GridCells.Fixed(MAX_COLUMN_COUNT),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false
                ) {
                    itemsIndexed(
                        items = eggList,
                        key = { _, item -> item.eggId },
                    ) { _, item ->
                        EggGridItemComponent(egg = item)
                    }
                }
            } else {
                EmptyGainEggView()
            }
        }
    }
}

@Composable
fun EmptyGainEggView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 138.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.empty_gain_egg),
            style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray400),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(R.drawable.egg_empty),
            stringResource(R.string.desc_egg_empty)
        )
    }
}

@PreviewScreenSizes
@Composable
fun PreviewGainEggScreen() {
    WalkieTheme {
        GainEggScreen(
            listOf(
                MyEggModel(
                    eggId = 0,
                    nowStep = 8334,
                    needStep = 10000,
                    play = true,
                    characterId = -1,
                    eggKind = EggKind.Legend
                ), MyEggModel(
                    eggId = 3,
                    nowStep = 8334,
                    needStep = 10000,
                    play = true,
                    characterId = -1,
                    eggKind = EggKind.Legend
                ), MyEggModel(
                    eggId = 2,
                    nowStep = 8334,
                    needStep = 10000,
                    play = false,
                    characterId = -1,
                    eggKind = EggKind.Epic
                )
            )
        )
    }
}


private const val MAX_COLUMN_COUNT = 2
