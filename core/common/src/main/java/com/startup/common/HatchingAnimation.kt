package com.startup.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import android.os.VibrationEffect
import android.os.Vibrator
import com.startup.common.util.OsVersions
import com.startup.ui.WalkieTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun EggHatchingAnimation(
    character: String = "해파리",
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 애니메이션 상태 관리
    var showFirstText by remember { mutableStateOf(false) }
    var showSecondText by remember { mutableStateOf(false) }
    var showEggLottie by remember { mutableStateOf(false) }
    var playEggCrackingLottie by remember { mutableStateOf(false) }
    var showExplosionLottie by remember { mutableStateOf(false) }
    var showCharacterImage by remember { mutableStateOf(false) }
    var showGlowImage by remember { mutableStateOf(false) }
    var canDismiss by remember { mutableStateOf(false) }
    val vibrationEnabled = remember { mutableStateOf(false) }

    // 진동 애니메이션 (캐릭터 부화시)
    val vibrateAnim = remember { Animatable(0f) }


    val eggCrackingComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset("walkie_Confetti.json")
    )

    //todo 로티 교체 캐릭터 등급 마다 로티 변경
    val explosionComposition by rememberLottieComposition(
        LottieCompositionSpec.Asset("walkie_EggBlue.json")
    )

    // 애니메이션 상태
    val eggCrackingAnimationState by animateLottieCompositionAsState(
        composition = eggCrackingComposition,
        isPlaying = playEggCrackingLottie,
        iterations = 1,
        restartOnPlay = false,
        speed = 1f
    )
    val explosionAnimationState by animateLottieCompositionAsState(
        composition = explosionComposition,
        isPlaying = showExplosionLottie,
        iterations = 1,
        restartOnPlay = false,
        speed = 1f
    )

    // 애니메이션 시퀀스 실행
    LaunchedEffect(Unit) {
        runAnimationSequence(
            coroutineScope = coroutineScope,
            context = context,
            vibrateAnim = vibrateAnim,
            onShowFirstText = { showFirstText = it },
            onShowSecondText = { showSecondText = it },
            onShowEggLottie = { showEggLottie = it },
            onPlayEggCrackingLottie = { playEggCrackingLottie = it },
            onShowExplosionLottie = { showExplosionLottie = it },
            onShowCharacterImage = { showCharacterImage = it },
            onShowGlowImage = { showGlowImage = it },
            onCanDismiss = { canDismiss = it },
            onVibrationEnabled = { vibrationEnabled.value = it }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.black.copy(alpha = 0.6f))
            .clickable(enabled = canDismiss) { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(1f + (vibrateAnim.value * 0.05f)), // 진동 효과
            contentAlignment = Alignment.Center
        ) {
            AnimatedText(
                text = stringResource(R.string.hatching_wait),
                visible = showFirstText,
                style = WalkieTheme.typography.head2,
                yOffset = (-168).dp
            )

            AnimatedText(
                text = stringResource(R.string.hatching_coming_soon),
                visible = showSecondText,
                style = WalkieTheme.typography.head2,
                yOffset = (-132).dp
            )

            // 로티 영역 - 정중앙
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                // 알 깨지는 로티
                AnimatedLottie(
                    composition = eggCrackingComposition,
                    progress = eggCrackingAnimationState,
                    visible = showEggLottie,
                    size = 200.dp
                )

                // 팡 터지는 로티
                AnimatedLottie(
                    composition = explosionComposition,
                    progress = explosionAnimationState,
                    visible = showExplosionLottie,
                    size = 250.dp
                )
            }

            // 결과 영역 - 캐릭터와 텍스트
            if (showCharacterImage) {
                AnimatedResultSection(
                    character = character,
                    visible = showCharacterImage
                )
            }

            // 글로우 이미지 (전체)
            AnimatedGlowImage(
                visible = showGlowImage,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        }
    }
}

/**
 * 애니메이션 시퀀스 실행 함수
 */
suspend fun runAnimationSequence(
    coroutineScope: CoroutineScope,
    context: Context,
    vibrateAnim: Animatable<Float, AnimationVector1D>,
    onShowFirstText: (Boolean) -> Unit,
    onShowSecondText: (Boolean) -> Unit,
    onShowEggLottie: (Boolean) -> Unit,
    onPlayEggCrackingLottie: (Boolean) -> Unit,
    onShowExplosionLottie: (Boolean) -> Unit,
    onShowCharacterImage: (Boolean) -> Unit,
    onShowGlowImage: (Boolean) -> Unit,
    onCanDismiss: (Boolean) -> Unit,
    onVibrationEnabled: (Boolean) -> Unit
) {
    // 0.4초 후 첫 번째 텍스트 표시
    delay(400)
    onShowFirstText(true)

    // 0.7초 후 두 번째 텍스트 표시
    delay(300)
    onShowSecondText(true)

    // 1.3초 후 알 깨지는 로티 등장
    delay(600)
    onShowEggLottie(true)

    // 1.8초 후 알 깨지는 로티 재생
    delay(500)
    onPlayEggCrackingLottie(true)

    // 3.7초 후 첫 번째 애니메이션 종료 및 두 번째 애니메이션 시작
    delay(1900)
    // 모든 텍스트와 로티 숨기기
    onShowFirstText(false)
    onShowSecondText(false)
    onShowEggLottie(false)

    // 팡 터지는 로티 시작 및 진동 효과
    onShowExplosionLottie(true)
    onVibrationEnabled(true)

    // 진동 효과 실행
    coroutineScope.launch {
        triggerVibration(context)

        // 진동 애니메이션 시작
        animateVibration(vibrateAnim)
    }

    // 3.9초 후 캐릭터 이미지와 텍스트 표시
    delay(200)
    onShowCharacterImage(true)

    // 4초 후 글로우 이미지 표시
    delay(100)
    onShowGlowImage(true)

    // 4초 후 종료 가능
    onCanDismiss(true)
}

/**
 * 진동 애니메이션 실행 함수
 */
suspend fun animateVibration(vibrateAnim: Animatable<Float, AnimationVector1D>) {
    vibrateAnim.animateTo(
        targetValue = 1f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 100),
            repeatMode = RepeatMode.Reverse
        )
    )
    vibrateAnim.animateTo(0f)
}

@SuppressLint("MissingPermission")
fun triggerVibration(context: Context) {
    if (OsVersions.isGreaterThanOrEqualsS()) {
        val vibratorManager =
            context.getSystemService(android.os.VibratorManager::class.java)
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
    } else {
        val vibrator =
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (OsVersions.isGreaterThanOrEqualsO()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    50,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(50)
        }
    }
}

/**
 * 애니메이션 텍스트 컴포넌트
 */
@Composable
fun AnimatedText(
    text: String,
    visible: Boolean,
    style: TextStyle,
    yOffset: Dp,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
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
                color = WalkieTheme.colors.white.copy(alpha = alpha),
                style = style,
                modifier = modifier,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * 애니메이션 로티 컴포넌트
 */
@Composable
fun AnimatedLottie(
    composition: LottieComposition?,
    progress: Float,
    visible: Boolean,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = ""
    )

    if (alpha > 0) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = modifier
                .size(size)
                .alpha(alpha)
        )
    }
}

/**
 * 결과 표시 섹션
 */
@Composable
fun AnimatedResultSection(
    character: String,
    visible: Boolean
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = ""
    )

    if (alpha > 0) {
        // 텍스트 먼저 표시 (위쪽)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-148).dp)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.hatching_result, character),
                color = WalkieTheme.colors.white,
                style = WalkieTheme.typography.head2,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha),
            contentAlignment = Alignment.Center
        ) {
            //todo 이미지 캐릭터 이미지
            /*
            Image(
                painter = painterResource(id = R.drawable.character_jellyfish),
                contentDescription = "Character",
                modifier = Modifier.fillMaxSize()
            )
            */
        }
    }
}

@Composable
fun AnimatedGlowImage(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(200),
        label = ""
    )

    if (alpha > 0) {
        Image(
            painter = painterResource(id = R.drawable.img_glow),
            contentDescription = null,
            modifier = modifier.alpha(alpha)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EggHatchingAnimationPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        // 기존 EggHatchingAnimation 함수 호출
        WalkieTheme {
            EggHatchingAnimation(
                onDismiss = {}
            )
        }
    }
}