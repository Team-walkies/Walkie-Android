package com.startup.common.animation

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.startup.common.util.OsVersions
import com.startup.common.util.triggerVibration
import com.startup.design_system.LottieAssets
import com.startup.design_system.ui.WalkieTheme
import com.startup.design_system.widget.button.PrimaryButton
import kotlinx.coroutines.delay
import com.startup.common.R

// 애니메이션 타이밍 상수
private const val ANIMATION_DURATION = 300
private const val CONGRATULATION_START_DELAY = 400L
private const val GOAL_TEXT_START_DELAY = 200L
private const val TEXT_HIDE_DELAY = 1200L
private const val EGG_DESCRIPTION_START_DELAY = 200L
private const val EGG_IMAGE_START_DELAY = 200L
private const val BUTTON_START_DELAY = 100L

// 애니메이션 위치 상수
private const val CONGRATULATION_TEXT_OFFSET = -50
private const val GOAL_TEXT_OFFSET = -10
private const val EGG_ACQUISITION_TEXT_OFFSET = -168
private const val EGG_DESCRIPTION_TEXT_OFFSET = -138
private const val EGG_IMAGE_SIZE = 200
private const val EGG_MOVE_DISTANCE = 55f
private const val LOTTIE_EXTRA_WIDTH = 40

/**
 * 애니메이션 상태를 관리하는 데이터 클래스
 */
data class AnimationState(
    val showCongratulationText: Boolean = false,
    val showGoalText: Boolean = false,
    val showLottie: Boolean = false,
    val showEggAcquisitionText: Boolean = false,
    val showEggDescription: Boolean = false,
    val showEggImage: Boolean = false,
    val showButton: Boolean = false
)

/**
 * 걸음 목표 달성 시 표시되는 축하 애니메이션 컴포넌트
 *
 * 애니메이션 시퀀스:
 * 1. 축하 모션 (0.4초~1.8초)
 *    - 0.4초: "축하해요!" 텍스트 표시
 *    - 0.4초: 진동 효과 + 팡 터지는 Lottie 애니메이션 재생
 *    - 0.6초: "목표 걸음을 다 채웠어요" 텍스트 표시
 *    - 1.8초: 모든 텍스트 사라짐
 *
 * 2. 알 획득 모션 (1.8초~2.3초)
 *    - 1.8초: "{알등급}알 획득!" 텍스트 표시
 *    - 2.0초: "걸어서 알을 부화시켜보세요" 텍스트 표시
 *    - 2.2초: 알 이미지 등장 (y+55 → y 위치로 이동)
 *    - 2.3초: "확인" 버튼 표시
 *
 * @param eggRank 알 등급 (-1: 빈알, 0: 일반, 1: 희귀, 2: 에픽, 3: 전설)
 * @param eggImageResId 알 이미지 리소스 ID
 * @param onDismiss 애니메이션 종료 시 호출되는 콜백
 */
@SuppressLint("MissingPermission")
@Composable
fun StepGoalAchievementAnimation(
    eggRank: Int = 0, // -1: Empty, 0: Normal, 1: Rare, 2: Epic, 3: Legend
    @DrawableRes eggImageResId: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // 애니메이션 상태 관리
    var animationState by remember { mutableStateOf(AnimationState()) }
    var canDismiss by remember { mutableStateOf(false) }

    // Lottie 애니메이션
    val confettiComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset(LottieAssets.CONFETTI_COLORED)
    )

    val confettiAnimationState by animateLottieCompositionAsState(
        composition = confettiComposition,
        isPlaying = animationState.showLottie,
        iterations = 1,
        restartOnPlay = false,
        speed = 1f
    )

    // 애니메이션 시퀀스 실행
    LaunchedEffect(Unit) {
        runStepGoalAnimationSequence(
            context = context,
            onUpdateState = { animationState = it },
            onCanDismiss = { canDismiss = it }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.blue50)
            .clickable(enabled = canDismiss) { onDismiss() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 팡 터지는 로티 - 정중앙 (텍스트 뒤에 배치)
            Box(
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                val lottieSize = screenWidth + LOTTIE_EXTRA_WIDTH.dp // 양옆 20dp씩

                AnimatedLottie(
                    composition = confettiComposition,
                    progress = confettiAnimationState,
                    visible = animationState.showLottie,
                    size = lottieSize
                )
            }

            // 축하 모션 텍스트들
            AnimatedTextWithOffset(
                text = stringResource(R.string.step_goal_achievement_congratulations),
                visible = animationState.showCongratulationText,
                yOffset = CONGRATULATION_TEXT_OFFSET.dp,
                textColor = WalkieTheme.colors.gray700,
                style = WalkieTheme.typography.head2
            )

            AnimatedTextWithOffset(
                text = stringResource(R.string.step_goal_achievement_goal_complete),
                visible = animationState.showGoalText,
                yOffset = GOAL_TEXT_OFFSET.dp,
                textColor = WalkieTheme.colors.gray700,
                style = WalkieTheme.typography.head2
            )

            // 알 획득 모션 텍스트들
            AnimatedTextWithOffset(
                text = stringResource(R.string.step_goal_achievement_egg_obtained, getEggRankName(context, eggRank)),
                visible = animationState.showEggAcquisitionText,
                yOffset = EGG_ACQUISITION_TEXT_OFFSET.dp,
                textColor = WalkieTheme.colors.gray700,
                style = WalkieTheme.typography.head2
            )

            AnimatedTextWithOffset(
                text = stringResource(R.string.step_goal_achievement_egg_description),
                visible = animationState.showEggDescription,
                yOffset = EGG_DESCRIPTION_TEXT_OFFSET.dp,
                textColor = WalkieTheme.colors.gray500,
                style = WalkieTheme.typography.body1
            )

            // 알 이미지 - 화면 정중앙에서 55dp 아래에서 시작해서 정중앙으로 이동
            Box(
                modifier = Modifier.align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                AnimatedEggImage(
                    eggImageResId = eggImageResId,
                    visible = animationState.showEggImage,
                    eggRank = eggRank,
                    context = context
                )
            }

            // 확인 버튼
            if (animationState.showButton) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 40.dp)
                ) {
                    val buttonAlpha by animateFloatAsState(
                        targetValue = if (animationState.showButton) 1f else 0f,
                        animationSpec = tween(ANIMATION_DURATION),
                        label = ""
                    )

                    PrimaryButton(
                        text = stringResource(R.string.step_goal_achievement_confirm),
                        modifier = Modifier.alpha(buttonAlpha),
                        onClick = onDismiss
                    )
                }
            }
        }
    }
}

/**
 * 걸음 목표 달성 애니메이션 시퀀스 실행 함수
 */
suspend fun runStepGoalAnimationSequence(
    context: Context,
    onUpdateState: (AnimationState) -> Unit,
    onCanDismiss: (Boolean) -> Unit
) {
    var currentState = AnimationState()

    // <축하해요 모션>
    // 텍스트 (축하해요!) : start 0.4초, opacity duration 0.3
    delay(CONGRATULATION_START_DELAY)
    currentState = currentState.copy(showCongratulationText = true)
    onUpdateState(currentState)

    // vibrate 효과 : start 0.4초
    triggerVibration(context)

    // Lottie 등장, 재생 (팡 터지는) : start 0.4초, 한 번만 재생
    currentState = currentState.copy(showLottie = true)
    onUpdateState(currentState)

    // 텍스트 (목표 걸음을 ~) : start 0.6초, opacity duration 0.3
    delay(GOAL_TEXT_START_DELAY)
    currentState = currentState.copy(showGoalText = true)
    onUpdateState(currentState)

    // 앞의 모든 텍스트 : end 1.8초, opacity duration 0.3
    delay(TEXT_HIDE_DELAY)
    currentState = currentState.copy(showCongratulationText = false, showGoalText = false)
    onUpdateState(currentState)

    // <알 획득 모션>
    // 텍스트 (~알 획득!) : start 1.8초, opacity duration 0.3
    currentState = currentState.copy(showEggAcquisitionText = true)
    onUpdateState(currentState)

    // 텍스트 (걸어서 알을 ~) : start 2초, opacity duration 0.3
    delay(EGG_DESCRIPTION_START_DELAY)
    currentState = currentState.copy(showEggDescription = true)
    onUpdateState(currentState)

    // 알 이미지 : start 2.2초, opacity duration 0.3
    // 알 이미지 이동 : start 2.2초, '정중앙 y값+55'에서 정중앙 y값으로 이동, ease out duration 0.3
    delay(EGG_IMAGE_START_DELAY)
    currentState = currentState.copy(showEggImage = true)
    onUpdateState(currentState)

    // 버튼 나타남 : start 2.3초, opacity duration 0.3
    delay(BUTTON_START_DELAY)
    currentState = currentState.copy(showButton = true)
    onUpdateState(currentState)
    onCanDismiss(true)
}



/**
 * 애니메이션 알 이미지 컴포넌트
 */
@Composable
fun AnimatedEggImage(
    @DrawableRes eggImageResId: Int,
    visible: Boolean,
    eggRank: Int,
    context: Context
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(ANIMATION_DURATION),
        label = ""
    )

    // 정중앙 y값+55에서 정중앙 y값으로 이동하는 애니메이션
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else EGG_MOVE_DISTANCE,
        animationSpec = tween(ANIMATION_DURATION, easing = androidx.compose.animation.core.EaseOut),
        label = ""
    )

    if (alpha > 0) {
        Box(
            modifier = Modifier
                .size(EGG_IMAGE_SIZE.dp)
                .offset(y = offsetY.dp)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = eggImageResId),
                contentDescription = "${getEggRankName(context, eggRank)}알",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 알 등급 숫자를 이름으로 변환
 */
fun getEggRankName(context: Context, eggRank: Int): String {
    return when (eggRank) {
        1 -> context.getString(R.string.clazz_rare)
        2 -> context.getString(R.string.clazz_epic)
        3 -> context.getString(R.string.clazz_legend)
        else -> context.getString(R.string.clazz_normal)
    }
}


/**
 * 위치 오프셋을 가진 애니메이션 텍스트 컴포넌트
 */
@Composable
private fun AnimatedTextWithOffset(
    text: String,
    visible: Boolean,
    yOffset: Dp,
    textColor: androidx.compose.ui.graphics.Color,
    style: TextStyle
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(ANIMATION_DURATION),
        label = ""
    )

    if (alpha > 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = yOffset),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor.copy(alpha = alpha),
                style = style,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StepGoalAchievementAnimationPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        WalkieTheme {
            StepGoalAchievementAnimation(
                eggRank = 1,
                eggImageResId = R.drawable.egg_legend, // 임시 이미지
                onDismiss = {}
            )
        }
    }
}
