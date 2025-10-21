package com.startup.stepcounter.service

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.startup.common.event.EventContainer
import com.startup.common.util.OsVersions
import com.startup.common.util.Printer
import com.startup.common.util.UsePermissionHelper.isGrantedPermissions
import com.startup.domain.provider.StepDataStore
import com.startup.stepcounter.broadcastReciver.RestartServiceReceiver
import com.startup.stepcounter.notification.NotificationCode.WALKIE_STEP_NOTIFICATION_ID
import com.startup.stepcounter.notification.buildWalkieNotification
import com.startup.stepcounter.notification.sendDailyGoalAchievedNotification
import com.startup.stepcounter.notification.sendHatchingNotification
import com.startup.stepcounter.notification.showPermissionNotification
import com.startup.stepcounter.notification.updateStepNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class WalkieStepForegroundService @Inject constructor() : Service(), SensorEventListener {

    @Inject
    lateinit var stepDataStore: StepDataStore
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    private val stepDetector = StepDetector()
    private val sensorManager: SensorManager? by lazy { getSystemService(Context.SENSOR_SERVICE) as? SensorManager? }
    private var hasStepSensor = false
    private var stepCounterSensor: Sensor? = null
    private var lastSensorValue: Int = 0  // 센서의 마지막 누적값 저장용
    private var isExecuteDieNotificationAlarm = false

    override fun onCreate() {
        super.onCreate()
        initStepSensor()
        checkAndStartForeground()
        observeNotificationUpdateEvents()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkAndStartForeground()
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        if (hasStepSensor) {
            handleStepSensor(event)
        } else {
            handleAccelerometerSensor(event)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return Unit
    }

    /**
     * 신체권한이 없으면 걸음수 측정이 안되므로 해당 권한 검사 후 서비스 실행
     * 권한 없을 경우 권한 허용 알림 생성
     */
    private fun checkAndStartForeground() {
        if (!OsVersions.isGreaterThanOrEqualsQ() ||
            isGrantedPermissions(this, Manifest.permission.ACTIVITY_RECOGNITION)
        ) {
            try {
                serviceScope.launch {
                    val currentSteps = stepDataStore.getEggCurrentSteps()
                    val targetStep = stepDataStore.getHatchingTargetStep()

                    ServiceCompat.startForeground(
                        this@WalkieStepForegroundService,
                        WALKIE_STEP_NOTIFICATION_ID,
                        buildWalkieNotification(
                            this@WalkieStepForegroundService,
                            currentSteps,
                            targetStep
                        ),
                        getCustomForegroundServiceType()
                    )
                }
            } catch (e: Exception) {
                Printer.e("WalkieStepService", "Failed to start foreground service: ${e.message}")
                stopSelf()
            }
        } else {
            showPermissionNotification(this)
            stopSelf()
        }
    }

    private fun startForegroundService() {
        serviceScope.launch {
            buildWalkieNotification(
                this@WalkieStepForegroundService,
                stepDataStore.getEggCurrentSteps(),
                stepDataStore.getHatchingTargetStep()
            )
        }
    }

    /**
    걸음수 측정을 위한 sensor 초기화
    보통의 경우 TYPE_STEP_COUNTER의 기기 걸음수 센서 사용
    기기의 걸음수 센서가 지원되지 않는 경우 TYPE_ACCELEROMETER 를 통해 자체 걸음수 감지 로직 사용
     */
    private fun initStepSensor() {
        val sensorType = if (sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            Sensor.TYPE_STEP_COUNTER
        } else {
            Sensor.TYPE_ACCELEROMETER
        }

        stepCounterSensor = sensorManager?.getDefaultSensor(sensorType)
        hasStepSensor = sensorType == Sensor.TYPE_STEP_COUNTER

        sensorManager?.registerListener(
            this,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )?.let { isRegistered ->
            if (!isRegistered) {
                // 센서 등록 실패 처리
                hasStepSensor = false
            }
        }
    }

    private fun observeNotificationUpdateEvents() {
        serviceScope.launch {
            EventContainer.updateNotificationFlow.collect { targetStep ->
                updateStepNotification(
                    this@WalkieStepForegroundService,
                    stepDataStore.getEggCurrentSteps(),
                    targetStep
                )
            }
        }
    }


    /**
     * 걸음수 저장 및 notification에 걸음수 업데이트
     */
    private fun handleSteps(context: Context, eggCurrentStep: Int) {
        serviceScope.launch {
            stepDataStore.saveEggCurrentSteps(eggCurrentStep)
            val targetSteps = stepDataStore.getHatchingTargetStep()
            val isAlreadyReached = stepDataStore.isTargetReached()

            // 알 부화 목표 달성 체크
            if (targetSteps in 1..eggCurrentStep && !isAlreadyReached) {
                stepDataStore.setTargetReached(true)
                EventContainer.triggerHatchingAnimation()
                sendHatchingNotification(context)
            }

            // 일일 목표 달성 체크
            val todaySteps = stepDataStore.getTodaySteps()
            val dailyTargetSteps = stepDataStore.getTodayWalkTargetStep()
            val isDailyGoalAlreadyReached = stepDataStore.isDailyGoalReached()

            if (dailyTargetSteps > 0 && todaySteps >= dailyTargetSteps && !isDailyGoalAlreadyReached) {
                stepDataStore.setDailyGoalReached(true)
                sendDailyGoalAchievedNotification(context)
            }

            updateStepNotification(context = context, steps = eggCurrentStep, target = targetSteps)
        }
    }

    private fun handleStepSensor(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val currentSensorValue = event.values[0].toInt()

            // 첫 실행시 초기화 curretSensorValue에서는 걸음수 센서에 누적된 걸음수를 받아옴
            // 재부팅시 초기화
            if (lastSensorValue == 0) {
                lastSensorValue = currentSensorValue
                return
            }

            // curretSensorValue의 값과 이전 저장시점 걸음수의 증가분만 계산
            val stepGap = currentSensorValue - lastSensorValue
            if (stepGap > 0) {
                serviceScope.launch {

                    stepDataStore.checkAndResetForNewDay()
                    val currentEggSteps = stepDataStore.getEggCurrentSteps()
                    val newSteps = currentEggSteps + stepGap

                    stepDataStore.addTodaySteps(stepGap)

                    handleSteps(
                        this@WalkieStepForegroundService,
                        eggCurrentStep = newSteps,
                    )
                }
            }
            lastSensorValue = currentSensorValue
        }
    }

    private fun handleAccelerometerSensor(event: SensorEvent) {
        stepDetector.handleAccelerometerSensor(event) {
            handleSteps(this, 0)
        }
    }

    override fun onDestroy() {
        if (isExecuteDieNotificationAlarm) {
            scheduleRestart()
        }
        sensorManager?.unregisterListener(this)
        super.onDestroy()
    }

    /**
    포그라운드 서비스 종료시, 재실행 로직
     */
    private fun scheduleRestart() {
        if (!isExecuteDieNotificationAlarm) return

        try {
            val restartTime = System.currentTimeMillis() + 3000 // 3초 후

            val intent = Intent(this, RestartServiceReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            (getSystemService(Context.ALARM_SERVICE) as? AlarmManager)?.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                restartTime,
                pendingIntent
            )
        } catch (e: Exception) {
            Printer.e("WalkieStepService", e.toString())
        }
    }

    /**
     * 안드로이드 14에 추가된 포그라운드 서비스 유형 명시
     */
    private fun getCustomForegroundServiceType(): Int {
        return if (OsVersions.isGreaterThanOrEqualsUPSIDEDOWNCAKE()) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
        } else {
            0
        }
    }
}