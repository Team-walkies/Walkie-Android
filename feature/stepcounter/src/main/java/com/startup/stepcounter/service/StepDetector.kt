package com.startup.stepcounter.service

import android.hardware.SensorEvent
import android.hardware.SensorManager

/**
 * 걸음 수를 감지하는 클래스
 * 가속도 센서의 데이터를 분석하여 걸음을 감지하고 카운트합니다.
 * 기기에 걸음수 감지 센서가 없는 경우에만 사용합니다.
 */
internal class StepDetector {
    // 각 축의 이전 센서값을 저장하는 배열
    private val lastValues = FloatArray(3)

    // 각 축의 이전 방향을 저장하는 배열 (1: 증가, -1: 감소, 0: 유지)
    private val lastDirections = FloatArray(3)

    // 각 축의 최대/최소 극값을 저장하는 2차원 배열
    // [0]: 최대값 배열, [1]: 최소값 배열
    private val lastExtremes = arrayOf(FloatArray(3), FloatArray(3))

    // 각 축의 이전 극값 차이를 저장하는 배열
    private val lastDiff = FloatArray(3)

    // 마지막으로 매칭된 극값의 타입 (-1: 없음, 0: 최대값, 1: 최소값)
    private var lastMatch = -1

    // 마지막 걸음이 감지된 시간 (밀리초)
    private var lastSensorMillis: Long = 0

    // 센서값 보정을 위한 Y축 오프셋
    private var yOffset: Float = 0f

    // 센서값 보정을 위한 스케일 팩터
    private val scale = FloatArray(2)

    companion object {
        // 걸음으로 인정할 기본 임계값
        private const val DEFAULT_THRESHOLD = 12.5f

        // 연속된 걸음 사이의 최소 시간 간격 (밀리초)
        private const val MIN_STEP_DELAY = 250L

        // 방향 변화를 감지할 때 사용할 임계값
        private const val DIRECTION_CHANGE_THRESHOLD = 2.0f

        // 연속 걸음으로 인정할 최소 횟수
        private const val CONTINUOUS_STEPS_THRESHOLD = 4
    }

    // 현재까지 감지된 연속 걸음 수
    private var continuousStepsCount = 0

    // 이전 걸음의 패턴 (1: 강한 걸음, -1: 약한 걸음)
    private var lastStepPattern = 0

    // 현재 설정된 감도 (기본값 사용)
    private var sensitivity = DEFAULT_THRESHOLD

    init {
        // 센서값 보정을 위한 초기화
        val h = 480  // 기준 높이값
        yOffset = h * 0.5f
        // 중력 가속도와 지구 자기장 기준으로 스케일 계수 계산
        scale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)))
        scale[1] = -(h * 0.5f * (1.0f / SensorManager.MAGNETIC_FIELD_EARTH_MAX))
    }

    /**
     * 걸음 감지 감도를 설정합니다.
     * @param newSensitivity 새로운 감도값 (높을수록 덜 민감)
     */
    fun setSensitivity(newSensitivity: Float) {
        sensitivity = newSensitivity
    }

    /**
     * 가속도 센서의 데이터를 처리하여 걸음을 감지합니다.
     * @param event 가속도 센서의 이벤트 데이터
     * @param onStepDetected 걸음이 감지되었을 때 호출될 콜백 함수
     */
    fun handleAccelerometerSensor(event: SensorEvent, onStepDetected: () -> Unit) {
        val values = event.values
        var vSum = 0f

        // 3축 가속도 값에 보정을 적용하고 평균 계산
        for (i in 0..2) {
            val v = yOffset + values[i] * scale[1]
            vSum += v
        }
        val v = vSum / 3

        // 현재 움직임의 방향 결정
        // DIRECTION_CHANGE_THRESHOLD를 통해 노이즈 필터링
        val direction = when {
            v > lastValues[0] + DIRECTION_CHANGE_THRESHOLD -> 1f
            v < lastValues[0] - DIRECTION_CHANGE_THRESHOLD -> -1f
            else -> 0f
        }

        // 방향이 변경되었을 때 (이전 방향과 반대)
        if (direction == -lastDirections[0]) {
            // 현재 극값의 타입 결정 (0: 최대값, 1: 최소값)
            val extType = if (direction > 0) 0 else 1
            lastExtremes[extType][0] = lastValues[0]

            // 최대값과 최소값의 차이 계산
            val diff = Math.abs(lastExtremes[extType][0] - lastExtremes[1 - extType][0])

            // 차이가 임계값을 넘으면 걸음 감지 로직 실행
            if (diff > sensitivity) {
                // 이전 걸음과의 패턴 비교
                val isAlmostAsLargeAsPrevious = diff > lastDiff[0] * 2 / 3
                val isPreviousLargeEnough = lastDiff[0] > diff / 3
                val isNotContra = lastMatch != 1 - extType

                // 현재 걸음의 패턴 결정
                val currentPattern = if (diff > lastDiff[0]) 1 else -1
                val isConsistentPattern = currentPattern == lastStepPattern

                // 걸음 감지 조건 확인
                if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                    val currentMillis = System.currentTimeMillis()
                    val timeGap = currentMillis - lastSensorMillis

                    // 최소 시간 간격 조건 확인
                    if (timeGap > MIN_STEP_DELAY) {
                        // 연속된 걸음 패턴 확인
                        if (isConsistentPattern) {
                            continuousStepsCount++
                        } else {
                            continuousStepsCount = 1
                        }

                        // 걸음으로 인정하는 조건
                        // 1. 연속된 패턴이 임계값 이상이거나
                        // 2. 강한 움직임이 감지되었을 때
                        if (continuousStepsCount >= CONTINUOUS_STEPS_THRESHOLD ||
                            diff > sensitivity * 1.2f
                        ) {
                            onStepDetected()
                            lastSensorMillis = currentMillis
                            lastStepPattern = currentPattern
                        }
                    }
                    lastMatch = extType
                } else {
                    // 패턴이 깨졌을 때 초기화
                    lastMatch = -1
                    continuousStepsCount = 0
                }
            }
            lastDiff[0] = diff
        }
        lastDirections[0] = direction
        lastValues[0] = v
    }

    /**
     * 모든 상태값을 초기화합니다.
     * 비정상적인 상태나 서비스 재시작 시 호출하세요.
     */
    fun reset() {
        lastValues.fill(0f)
        lastDirections.fill(0f)
        lastDiff.fill(0f)
        lastExtremes.forEach { it.fill(0f) }
        lastMatch = -1
        lastSensorMillis = 0
        continuousStepsCount = 0
        lastStepPattern = 0
    }
}