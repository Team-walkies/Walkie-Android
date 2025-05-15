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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.startup.common.base.NavigationEvent
import com.startup.common.extension.orFalse
import com.startup.common.base.BaseUiState
import com.startup.common.extension.shimmerEffect
import com.startup.common.util.DateUtil
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.ui.WalkieTheme.colors
import com.startup.design_system.ui.noRippleClickable
import com.startup.design_system.widget.actionbar.PageActionBar
import com.startup.design_system.widget.actionbar.PageActionBarType
import com.startup.design_system.widget.bottom_sheet.WalkieDragHandle
import com.startup.design_system.widget.button.PrimaryButton
import com.startup.design_system.widget.tag.TagMedium
import com.startup.domain.model.character.CharacterObtainInfo
import com.startup.home.R
import com.startup.model.character.CharacterKind
import com.startup.model.character.WalkieCharacter
import com.startup.model.character.WalkieCharacterDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

object CharacterTabType {
    const val JELLYFISH = 0
    const val DINO = 1
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HatchingCharacterScreen(
    viewState: HatchingCharacterViewState,
    onClickPartner: (WalkieCharacter) -> Unit,
    onSelectPartner: (WalkieCharacterDetail) -> Unit,
    onDismissBottomSheet: () -> Unit,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.white)
    ) {
        PageActionBar(PageActionBarType.JustBackActionBarType {
            onNavigationEvent.invoke(NavigationEvent.Back)
        })

        val scrollState = rememberScrollState()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val characterDetailUiState by viewState.characterDetail.collectAsStateWithLifecycle()
        var viewingCharacter: WalkieCharacter? by remember { mutableStateOf(null) }
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
            CharacterTabsContent(viewState) {
                onClickPartner(it)
                viewingCharacter = it
            }
        }

        viewingCharacter?.let { preloadCharacter ->
            CharacterDetailBottomSheet(
                sheetState = sheetState,
                characterUiState = characterDetailUiState,
                picked = viewingCharacter?.picked.orFalse(),
                preloadCharacter = preloadCharacter,
                onDismiss = {
                    viewingCharacter = null
                    onDismissBottomSheet.invoke()
                },
                onSelectPartner = { character ->
                    viewingCharacter = null
                    onSelectPartner(character)
                }
            )
        }
    }
}


@Composable
fun CharacterTabsContent(
    viewState: HatchingCharacterViewState,
    onCharacterClick: (WalkieCharacter) -> Unit
) {
    val characterTypes = listOf(
        stringResource(R.string.character_jellyfish),
        stringResource(R.string.character_dino)
    )

    val pagerState =
        rememberPagerState(initialPage = CharacterTabType.JELLYFISH) { characterTypes.size }
    val coroutineScope = rememberCoroutineScope()

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
                viewState = viewState,
                tabIndex = page,
                onCharacterClick = { character ->
                    onCharacterClick.invoke(character)
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
    viewState: HatchingCharacterViewState,
    tabIndex: Int,
    onCharacterClick: (WalkieCharacter) -> Unit
) {
    val dinoState by viewState.dinoCharacterState.collectAsStateWithLifecycle()
    val jellyState by viewState.jellyfishCharacterState.collectAsStateWithLifecycle()
    when (tabIndex) {
        CharacterTabType.JELLYFISH -> JellyfishContent(
            jellyState,
            onCharacterClick = onCharacterClick
        )

        CharacterTabType.DINO -> DinosaurContent(
            dinoState,
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
    state: BaseUiState<List<WalkieCharacter>>,
    onCharacterClick: (WalkieCharacter) -> Unit
) {
    CharacterGrid(
        description = stringResource(id = R.string.character_jellyfish_description),
        charactersState = state,
        onCharacterClick = onCharacterClick,
    )
}

@Composable
fun DinosaurContent(
    state: BaseUiState<List<WalkieCharacter>>,
    onCharacterClick: (WalkieCharacter) -> Unit
) {
    CharacterGrid(
        description = stringResource(id = R.string.character_dino_description),
        charactersState = state,
        onCharacterClick = onCharacterClick,
    )
}

@Composable
fun CharacterGrid(
    description: String,
    charactersState: BaseUiState<List<WalkieCharacter>>,
    onCharacterClick: (WalkieCharacter) -> Unit,
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
            charactersState = charactersState,
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
private fun CharacterGridItems(
    charactersState: BaseUiState<List<WalkieCharacter>>,
    onCharacterClick: (WalkieCharacter) -> Unit,
    modifier: Modifier = Modifier
) {

    if (charactersState.isShowShimmer) {
        val rows = (0..7).chunked(2)
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
                            SkeletonCharacterComponent()
                        }
                    }
                }
            }
        }
    } else {
        val rows = charactersState.data.chunked(2)
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
                                isSelected = character.picked,
                                onClick = onCharacterClick
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
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: WalkieCharacter,
    isSelected: Boolean = false,
    onClick: (WalkieCharacter) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable {
                if (character.isHatched()) {
                    onClick(character)
                }
            }
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
            .padding(vertical = 12.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.ic_foot),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp),
                contentDescription = stringResource(R.string.desc_egg_play_icon),
                tint = colors.blue300
            )
        }

        CharacterItemContent(
            character = character,
        )
    }
}

@Composable
private fun CharacterItemContent(
    character: WalkieCharacter,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = if (!character.isHatched()) {
                    when (character.characterKind) {
                        CharacterKind.Jellyfish -> R.drawable.jellyfish_empty
                        CharacterKind.Dino -> R.drawable.dino_empty
                    }
                } else {
                    character.characterImageResId
                }
            ),
            contentDescription = stringResource(id = character.characterNameResId),
            modifier = Modifier
                .fillMaxWidth(0.6f) // 작은 기기 대응
                .sizeIn(maxWidth = 120.dp, maxHeight = 120.dp)
        )

        if (!character.isHatched()) {
            Text(
                text = stringResource(id = R.string.character_not_gain),
                style = WalkieTheme.typography.head5.copy(color = colors.gray500),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.character_gain_induction),
                style = WalkieTheme.typography.body2.copy(color = colors.gray400, textAlign = TextAlign.Center),
            )
        } else {
            Text(
                text = stringResource(id = character.characterNameResId),
                style = WalkieTheme.typography.head5.copy(color = colors.gray700),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = stringResource(R.string.format_int, character.count),
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
    preloadCharacter: WalkieCharacter,
    characterUiState: BaseUiState<WalkieCharacterDetail?>,
    picked: Boolean,
    onDismiss: () -> Unit,
    onSelectPartner: (WalkieCharacterDetail) -> Unit
) {
    val density = LocalDensity.current
    val screenHeight = with(density) {
        LocalConfiguration.current.screenHeightDp.dp
    }

    // 화면 상단에서 94dp만큼 떨어진 위치에서 시작하도록 계산
    val sheetHeight = screenHeight - 94.dp
    val character = characterUiState.data

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
            if (characterUiState.isShowShimmer) {
                SkeletonCharacterDetailComponent(preloadCharacter)
            } else if (character != null) {
                CharacterDetailContent(
                    character = character,
                    onSelectPartner = onSelectPartner,
                    picked = picked
                )
            }
        }
    }
}

@Composable
fun CharacterDetailContent(
    picked: Boolean,
    character: WalkieCharacterDetail,
    onSelectPartner: (WalkieCharacterDetail) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 30.dp, start = 16.dp, end = 16.dp)
    ) {
        CharacterDetailScrollableContent(
            characterDetail = character,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )

        Spacer(modifier = Modifier.height(6.dp))
        CharacterSelectButton(
            character = character,
            isAlreadySelected = picked,
            onSelectPartner = onSelectPartner
        )
    }
}

@Composable
private fun SkeletonCharacterDetailComponent(
    character: WalkieCharacter = WalkieCharacter.ofEmpty(),
    isAlreadySelected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(colors.white)
            .padding(bottom = 30.dp, start = 16.dp, end = 16.dp)
    ) {
        Image(
            painter = painterResource(id = character.characterImageResId),
            contentDescription = stringResource(id = character.characterNameResId),
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = character.characterNameResId),
            style = WalkieTheme.typography.head3.copy(color = colors.gray700)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.character_detail_wise_saying),
            style = WalkieTheme.typography.body2.copy(color = colors.gray500),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(57.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(57.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
        val buttonText = if (isAlreadySelected) {
            stringResource(R.string.character_already_selected)
        } else {
            stringResource(R.string.character_partner_select)
        }

        Spacer(modifier = Modifier.weight(1F))
        PrimaryButton(
            text = buttonText,
        ) {}
    }
}

@Composable
private fun CharacterDetailScrollableContent(
    characterDetail: WalkieCharacterDetail,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = characterDetail.character.characterImageResId),
            contentDescription = stringResource(id = characterDetail.character.characterNameResId),
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = characterDetail.character.characterNameResId),
            style = WalkieTheme.typography.head3.copy(color = colors.gray700)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.character_detail_wise_saying),
            style = WalkieTheme.typography.body2.copy(color = colors.gray500),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        CharacterInfoTags(characterDetail)

        Spacer(modifier = Modifier.height(20.dp))

        CharacterHistoryList(characterDetail.obtainInfoList)
    }
}

@Composable
private fun CharacterInfoTags(characterDetail: WalkieCharacterDetail) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TagMedium(
            text = stringResource(characterDetail.character.rank.displayStrResId),
            textColor = characterDetail.character.rank.getTextColor()
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
private fun CharacterHistoryList(list: List<CharacterObtainInfo>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        list.forEach {
            CharacterHistoryItem(it)
        }
    }
}

@Composable
private fun CharacterHistoryItem(info: CharacterObtainInfo) {
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
                text = info.obtainedPosition,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                style = WalkieTheme.typography.body2.copy(color = colors.gray700)
            )

            Text(
                text = stringResource(
                    R.string.character_hatched_date_description,
                    DateUtil.convertDateFormat(info.obtainedDate)
                ),
                style = WalkieTheme.typography.body2.copy(color = colors.gray500)
            )
        }
    }
}

@Composable
private fun CharacterSelectButton(
    character: WalkieCharacterDetail,
    isAlreadySelected: Boolean,
    onSelectPartner: (WalkieCharacterDetail) -> Unit
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

@Composable
private fun PreviewHatchingCharacterScreen() {
    WalkieTheme {
        HatchingCharacterScreen(
            viewState = HatchingCharacterViewStateImpl(
                dinoCharacterState = MutableStateFlow(
                    BaseUiState(
                        isShowShimmer = true,
                        emptyList()
                    )
                ),
                jellyfishCharacterState = MutableStateFlow(
                    BaseUiState(
                        isShowShimmer = true,
                        emptyList()
                    )
                ),
                characterDetail = MutableStateFlow(
                    BaseUiState(
                        isShowShimmer = true, null
                    )
                ),
            ),
            onSelectPartner = {},
            onClickPartner = {},
            onDismissBottomSheet = {},
            onNavigationEvent = {},
        )
    }
}

