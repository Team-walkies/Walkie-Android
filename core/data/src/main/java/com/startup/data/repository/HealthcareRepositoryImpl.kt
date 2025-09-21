package com.startup.data.repository

import com.startup.common.extension.orZero
import com.startup.common.util.DateUtil
import com.startup.common.util.Printer
import com.startup.data.datasource.EggDataSource
import com.startup.data.datasource.HealthcareDataSource
import com.startup.data.remote.dto.request.egg.EggAwardGetRequest
import com.startup.domain.model.egg.GetEggAWard
import com.startup.domain.model.healthcare.DailyHealthcareDetail
import com.startup.domain.model.healthcare.DailyHealthcareListItem
import com.startup.domain.provider.StepDataStore
import com.startup.domain.repository.HealthcareRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.round

internal class HealthcareRepositoryImpl @Inject constructor(
    private val healthcareDataSource: HealthcareDataSource,
    private val eggDataSource: EggDataSource,
    private val stepDataStore: StepDataStore
) : HealthcareRepository {
    override fun postEggGet(getEggAward: GetEggAWard): Flow<Unit> = eggDataSource.postEggGet(
        EggAwardGetRequest(
            latitude = getEggAward.latitude,
            longitude = getEggAward.longitude,
            healthcareEggAcquiredAt = getEggAward.healthcareEggAcquiredAt
        )
    )

    override fun getCalendarHealthcareList(startDate: String, endDate: String): Flow<List<DailyHealthcareListItem>> {
        val now = LocalDate.now()
        return healthcareDataSource.getCalendarHealthcareList(startDate, endDate)
            .map { list ->
                list.map { item ->
                    val convertItem = item.toDomain()
                    if (convertItem.date.isEqual(now)) {
                        val currentSteps = stepDataStore.getTodaySteps()
                        val targetStep = stepDataStore.getTodayWalkTargetStep()
                        if (currentSteps > convertItem.nowSteps) {
                            convertItem.copy(
                                nowSteps = currentSteps,
                                targetSteps = targetStep,
                            )
                        } else {
                            convertItem
                        }
                    } else {
                        convertItem
                    }
                }
            }
    }

    override fun getCalendarHealthcareDetail(searchDate: LocalDate): Flow<DailyHealthcareDetail> =
        healthcareDataSource.getCalendarHealthcareDetail(DateUtil.convertTranslatorDateFormat(searchDate))
            .map { item ->
                val convertItem = item.toDomain()
                if (DateUtil.isSameDay(
                        time1 = searchDate
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli(),
                        time2 = System.currentTimeMillis()
                    )
                ) {
                    val currentSteps = stepDataStore.getTodaySteps()
                    val targetStep = stepDataStore.getTodayWalkTargetStep()
                    val distanceMeters = (round((0.0006 * currentSteps) * 100) / 100.0)
                    val calories = currentSteps / 30
                    if (currentSteps > convertItem.nowSteps) {
                        convertItem.copy(
                            nowCalories = calories,
                            nowDistance = distanceMeters,
                            nowSteps = currentSteps,
                            targetSteps = targetStep
                        )
                    } else {
                        convertItem
                    }
                } else {
                    convertItem
                }
            }

    override fun getTodayTargetStep(): Flow<Int> = stepDataStore.getTodayWalkTargetStepFlow()
    override suspend fun putTodayTargetStep(targetStep: Int) {
        stepDataStore.saveTodayWalkTargetStep(targetStep)
    }

    override fun getCalendarHealthcareContinueDays(): Flow<Int> =
        healthcareDataSource.getCalendarHealthcareContinueDays().map {
            it.continuousDays.orZero()
        }
}