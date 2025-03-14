package com.startup.home.egg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.home.R
import com.startup.home.character.CharacterKind
import com.startup.home.egg.model.EggKind
import com.startup.home.egg.model.EggOfCharacterGainProbabilityModel
import com.startup.ui.WalkieTheme
import kotlin.math.roundToInt

@Composable
fun EggGainProbabilityScreen(onNavigationEvent: (NavigationEvent) -> Unit) {
    val eggList = EggKind.entries.toTypedArray().filter { it != EggKind.Empty }
    val eggOfCharacterList = eggList.map { it.kindOfCharacterGainProbability() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)
    ) {
        val scrollState = rememberScrollState()
        PageActionBar(PageActionBarType.JustBackActionBarType {
            onNavigationEvent.invoke(NavigationEvent.Back)
        })
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            EggGainProbabilityContent(eggList)
            Spacer(modifier = Modifier.height(40.dp))
            EggOfCharacterGainProbabilityContent(eggOfCharacterList)
        }
    }
}

@Composable
private fun EggGainProbabilityContent(eggList: List<EggKind>) {
    Text(
        stringResource(R.string.egg_probability_title),
        style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        stringResource(R.string.egg_probability_sub_title),
        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        eggList.forEach { eggKind ->
            EggColumnItemComponent(eggKind)
        }
    }
}

@Composable
private fun EggOfCharacterGainProbabilityContent(eggList: List<EggOfCharacterGainProbabilityModel>) {
    Text(
        stringResource(R.string.egg_of_character_probability_title),
        style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        stringResource(R.string.egg_of_character_probability_sub_title),
        style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
    )
    Spacer(modifier = Modifier.height(20.dp))
    eggList.forEachIndexed { index, item ->
        EggOfCharacterGainColumnItemComponent(item)
        if (index != eggList.size - 1) {
            HorizontalDivider(thickness = 2.dp, color = WalkieTheme.colors.gray50)
        }
    }
    Spacer(modifier = Modifier.height(48.dp))
}

@Composable
internal fun EggOfCharacterGainColumnItemComponent(item: EggOfCharacterGainProbabilityModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(item.eggKind.drawableResId),
                contentDescription = stringResource(R.string.desc_egg),
                modifier = Modifier.size(120.dp),
            )
            Text(
                text = stringResource(item.eggKind.rankStrResId) + " " + stringResource(item.eggKind.eggStrResId),
                style = WalkieTheme.typography.body1.copy(color = item.eggKind.getTextColor())
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(
                item.normalProbability,
                item.rareProbability,
                item.epicProbability,
                item.legendProbability
            ).zip(CharacterKind.entries.toList()) { percent, character ->
                EggOfCharacterProbabilityLabel(
                    title = stringResource(character.displayStrResId)
                            + stringResource(character.characterStrResId),
                    percent = percent
                )
            }
        }
    }
}

@Composable
private fun EggOfCharacterProbabilityLabel(title: String, percent: Float) {
    val percentage = "${(percent * 100).roundToInt()}%"
    Row {
        Text(title, style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.gray500))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.width(50.dp),
            text = percentage,
            style = WalkieTheme.typography.head5.copy(color = WalkieTheme.colors.gray700)
        )
    }
}

@Composable
internal fun EggColumnItemComponent(
    egg: EggKind,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = WalkieTheme.colors.gray50,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(start = 8.dp, bottom = 8.dp, top = 8.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(egg.drawableResId),
            contentDescription = stringResource(R.string.desc_egg),
            modifier = Modifier.size(60.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.weight(1F)) {
            Text(
                text = stringResource(egg.rankStrResId) + " " + stringResource(egg.eggStrResId),
                style = WalkieTheme.typography.body1.copy(color = egg.getTextColor())
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(egg.eggInfoStrRedId),
                style = WalkieTheme.typography.body2.copy(color = WalkieTheme.colors.gray500)
            )
        }
        val percentage = "${(egg.gainProbability * 100).roundToInt()}%"
        Text(
            text = percentage,
            style = WalkieTheme.typography.head3.copy(color = WalkieTheme.colors.gray700)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEggGainProbabilityScreen() {
    WalkieTheme {
        EggGainProbabilityScreen({})
    }
}