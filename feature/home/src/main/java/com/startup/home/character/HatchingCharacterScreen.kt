package com.startup.home.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.common.base.NavigationEvent
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.design_system.widget.tag.TagMedium
import com.startup.home.R
import com.startup.home.character.model.CharacterFactory
import com.startup.home.character.model.BaseWalkieCharacter
import com.startup.ui.WalkieTheme
import com.startup.ui.WalkieTheme.colors
import com.startup.ui.noRippleClickable
import kotlinx.coroutines.launch

object CharacterTabType {
    const val JELLYFISH = 0
    const val DINO = 1
}

@Composable
fun HatchingCharacterScreen(onNavigationEvent: (NavigationEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.white)
    ) {
        PageActionBar(PageActionBarType.JustBackActionBarType {
            onNavigationEvent.invoke(NavigationEvent.Back)
        })

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.hatching_characters),
                style = WalkieTheme.typography.head2
            )
            Spacer(modifier = Modifier.height(22.dp))
            CharacterTabsContent()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterTabsContent() {
    val characterTypes = listOf(
        stringResource(R.string.character_jellyfish),
        stringResource(R.string.character_dino)
    )

    val pagerState =
        rememberPagerState(initialPage = CharacterTabType.JELLYFISH) { characterTypes.size }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var showBottomSheet by remember { mutableStateOf(false) }

    //todo state로 관리
    var selectedCharacter by remember { mutableStateOf<BaseWalkieCharacter?>(null) }
    var viewingCharacter by remember { mutableStateOf<BaseWalkieCharacter?>(null) }

    Column {
        CharacterTypeTabs(
            characterTypes = characterTypes,
            currentPage = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            CharacterContentByType(
                tabIndex = page,
                selectedCharacter = selectedCharacter,
                onCharacterClick = { character ->
                    viewingCharacter = character
                    showBottomSheet = true
                }
            )
        }

        if (showBottomSheet) {
            CharacterDetailBottomSheet(
                sheetState = sheetState,
                character = viewingCharacter,
                selectedCharacter = selectedCharacter,
                onDismiss = {
                    showBottomSheet = false
                },
                onSelectPartner = { character ->
                    selectedCharacter = character
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
private fun CharacterTypeTabs(
    characterTypes: List<String>,
    currentPage: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        characterTypes.forEachIndexed { index, type ->
            CharacterTab(
                text = type,
                selected = index == currentPage,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
private fun CharacterContentByType(
    tabIndex: Int,
    selectedCharacter: BaseWalkieCharacter?,
    onCharacterClick: (BaseWalkieCharacter) -> Unit
) {
    when (tabIndex) {
        CharacterTabType.JELLYFISH -> JellyfishContent(
            selectedCharacter = selectedCharacter,
            onCharacterClick = onCharacterClick
        )

        CharacterTabType.DINO -> DinosaurContent(
            selectedCharacter = selectedCharacter,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
fun CharacterTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text,
                style = WalkieTheme.typography.head3,
                color = if (selected) colors.gray700 else colors.gray400
            )
        }
    }
}

@Composable
fun JellyfishContent(
    selectedCharacter: BaseWalkieCharacter?,
    onCharacterClick: (BaseWalkieCharacter) -> Unit
) {
    val jellyfishCharacters = CharacterFactory.getJellyfishCharacters()

    // todo fetch
    // 첫 번째와 세 번째 캐릭터만 empty(미획득)로 가정
    val emptyCharacterIds =
        remember { listOf(jellyfishCharacters[0].id, jellyfishCharacters[2].id) }

    CharacterGrid(
        description = stringResource(id = R.string.character_jellyfish_description),
        characters = jellyfishCharacters,
        selectedCharacter = selectedCharacter,
        emptyCharacterIds = emptyCharacterIds,
        onCharacterClick = onCharacterClick,
        currentTab = CharacterTabType.JELLYFISH,
    )
}

@Composable
fun DinosaurContent(
    selectedCharacter: BaseWalkieCharacter?,
    onCharacterClick: (BaseWalkieCharacter) -> Unit
) {
    // todo fetch
    // 첫 번째와 세 번째 캐릭터만 empty(미획득)로 가정
    val dinoCharacters = CharacterFactory.getDinoCharacters()
    val emptyCharacterIds = remember { listOf(dinoCharacters[1].id, dinoCharacters[3].id) }

    CharacterGrid(
        description = stringResource(id = R.string.character_dino_description),
        characters = dinoCharacters,
        selectedCharacter = selectedCharacter,
        emptyCharacterIds = emptyCharacterIds,
        onCharacterClick = onCharacterClick,
        currentTab = CharacterTabType.DINO,
    )
}

@Composable
fun CharacterGrid(
    description: String,
    characters: List<BaseWalkieCharacter>,
    selectedCharacter: BaseWalkieCharacter?,
    emptyCharacterIds: List<String>,
    currentTab: Int,
    onCharacterClick: (BaseWalkieCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = description,
            style = WalkieTheme.typography.body2,
            color = colors.gray500,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        CharacterGridItems(
            characters = characters,
            selectedCharacter = selectedCharacter,
            emptyCharacterIds = emptyCharacterIds,
            currentTab = currentTab,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
private fun CharacterGridItems(
    characters: List<BaseWalkieCharacter>,
    selectedCharacter: BaseWalkieCharacter?,
    emptyCharacterIds: List<String>,
    currentTab: Int,
    onCharacterClick: (BaseWalkieCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = characters.chunked(2)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 48.dp)
    ) {
        rows.forEach { rowCharacters ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowCharacters.forEach { character ->
                    Box(modifier = Modifier.weight(1f)) {
                        CharacterItem(
                            character = character,
                            isSelected = character.id == selectedCharacter?.id,
                            isEmpty = emptyCharacterIds.contains(character.id),
                            currentTab = currentTab,
                            onClick = { onCharacterClick(character) }
                        )
                    }
                }

                // 마지막 행이 홀수 개일 경우 빈 공간 추가
                if (rowCharacters.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: BaseWalkieCharacter,
    isSelected: Boolean = false,
    isEmpty: Boolean = false,
    currentTab: Int = CharacterTabType.JELLYFISH,
    onClick: (BaseWalkieCharacter) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onClick(character) }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = colors.blue300,
                        shape = RoundedCornerShape(20.dp)
                    )
                } else Modifier
            )
            .aspectRatio(1f)
            .background(
                color = colors.gray100,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_foot),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp),
                contentDescription = stringResource(R.string.desc_egg_play_icon),
                tint = colors.blue300
            )
        }

        CharacterItemContent(
            character = character,
            isEmpty = isEmpty,
            currentTab = currentTab
        )
    }
}

@Composable
private fun CharacterItemContent(
    character: BaseWalkieCharacter,
    isEmpty: Boolean,
    currentTab: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = if (isEmpty) {
                    when (currentTab) {
                        CharacterTabType.JELLYFISH -> R.drawable.jellyfish_empty
                        CharacterTabType.DINO -> R.drawable.dino_empty
                        else -> character.imageResource
                    }
                } else {
                    character.imageResource
                }
            ),
            contentDescription = stringResource(id = character.nameResId),
            modifier = Modifier
                .fillMaxWidth(0.6f) // 작은 기기 대응
                .sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isEmpty) {
            Text(
                text = stringResource(id = R.string.character_not_gain),
                style = WalkieTheme.typography.head5.copy(color = colors.gray500),
            )
            Text(
                text = stringResource(id = R.string.character_gain_induction),
                style = WalkieTheme.typography.body2.copy(color = colors.gray400),
            )
        } else {
            Text(
                text = stringResource(id = character.nameResId),
                style = WalkieTheme.typography.head5.copy(color = colors.gray700),
            )
            Row {
                Text(
                    text = stringResource(R.string.format_int, 1), // 임시로 1로 고정
                    style = WalkieTheme.typography.head6.copy(color = colors.gray700),
                )
                Text(
                    text = stringResource(R.string.character_count),
                    style = WalkieTheme.typography.body2.copy(color = colors.gray500),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailBottomSheet(
    sheetState: SheetState,
    character: BaseWalkieCharacter?,
    selectedCharacter: BaseWalkieCharacter?,
    onDismiss: () -> Unit,
    onSelectPartner: (BaseWalkieCharacter) -> Unit
) {
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp
    }

    // 화면 상단에서 94dp만큼 떨어진 위치에서 시작하도록 계산
    val sheetHeight = screenHeight - 94.dp

    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            tonalElevation = 24.dp,
            containerColor = colors.white,
            dragHandle = {
                WalkieDragHandle()
            },
            contentColor = colors.white,
            scrimColor = colors.blackOpacity60,
            modifier = Modifier
                .height(sheetHeight)
                .align(Alignment.BottomCenter)
        ) {
            character?.let {
                CharacterDetailContent(
                    character = it,
                    selectedCharacter = selectedCharacter,
                    onSelectPartner = onSelectPartner
                )
            }
        }
    }
}

@Composable
fun CharacterDetailContent(
    character: BaseWalkieCharacter,
    selectedCharacter: BaseWalkieCharacter?,
    onSelectPartner: (BaseWalkieCharacter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp, start = 16.dp, end = 16.dp)
    ) {
        CharacterDetailScrollableContent(
            character = character,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )

        Spacer(modifier = Modifier.height(34.dp))
        CharacterSelectButton(
            character = character,
            isAlreadySelected = selectedCharacter?.id == character.id,
            onSelectPartner = onSelectPartner
        )
    }
}

@Composable
private fun CharacterDetailScrollableContent(
    character: BaseWalkieCharacter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = character.imageResource),
            contentDescription = stringResource(id = character.nameResId),
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = character.nameResId),
            style = WalkieTheme.typography.head3.copy(color = colors.gray700)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.character_detail_wise_saying),
            style = WalkieTheme.typography.body2.copy(color = colors.gray500),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CharacterInfoTags(character)

        Spacer(modifier = Modifier.height(20.dp))

        CharacterHistoryList()
    }
}

@Composable
private fun CharacterInfoTags(character: BaseWalkieCharacter) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TagMedium(
            text = stringResource(character.rarity.rankStrResId),
            textColor = character.rarity.getTextColor()
        )
        Row(
            modifier = Modifier
                .background(colors.gray100, shape = RoundedCornerShape(100.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.format_int, 1), // 임시로 1로 고정
                style = WalkieTheme.typography.head6.copy(color = colors.gray700),
            )
            Text(
                text = stringResource(R.string.character_gain_count),
                style = WalkieTheme.typography.body2.copy(color = colors.gray500),
            )
        }
    }
}

@Composable
private fun CharacterHistoryList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(10) {
            CharacterHistoryItem()
        }
    }
}

@Composable
private fun CharacterHistoryItem() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = colors.gray50
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color = colors.blue300)
            )

            Text(
                text = "서울시 서초구",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                style = WalkieTheme.typography.body2.copy(color = colors.gray700)
            )

            Text(
                text = "2025.01.13  부화",
                style = WalkieTheme.typography.body2.copy(color = colors.gray500)
            )
        }
    }
}

@Composable
private fun CharacterSelectButton(
    character: BaseWalkieCharacter,
    isAlreadySelected: Boolean,
    onSelectPartner: (BaseWalkieCharacter) -> Unit
) {
    val buttonText = if (isAlreadySelected) {
        stringResource(R.string.character_already_selected)
    } else {
        stringResource(R.string.character_partner_select)
    }

    PrimaryButton(
        text = buttonText,
        enabled = !isAlreadySelected
    ) {
        if (!isAlreadySelected) {
            onSelectPartner(character)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
@Preview(showBackground = true)
fun PreviewCharacterDetailBottomSheet() {
    WalkieTheme {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val characterFactory = CharacterFactory.getJellyfishCharacters()
        val dummyCharacter = characterFactory[0]
        val dummyCharacter2 = characterFactory[1]

        CharacterDetailBottomSheet(
            sheetState = sheetState,
            character = dummyCharacter,
            selectedCharacter = dummyCharacter2,
            onDismiss = {},
            onSelectPartner = {}
        )
    }
}

@PreviewScreenSizes
@Composable
@Preview
private fun PreviewHatchingCharacterScreen() {
    WalkieTheme {
        HatchingCharacterScreen() {}
    }
}