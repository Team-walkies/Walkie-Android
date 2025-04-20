package com.startup.common.util

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtil {
    fun convertDateFormat(dateStr: String): String = kotlin.runCatching {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
        val date = inputFormat.parse(dateStr) ?: return ""
        outputFormat.format(date)
    }.getOrNull().orEmpty()

    fun convertDateTimeFormat(dateStr: String): String = kotlin.runCatching {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
        val date = inputFormat.parse(dateStr) ?: return ""
        outputFormat.format(date)
    }.getOrNull().orEmpty()

    fun convertLocalDate(dateStr: String): LocalDate = kotlin.runCatching {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(dateStr, formatter)
    }.getOrNull() ?: LocalDate.now()

    fun convertLocalTime(dateStr: String): LocalTime = kotlin.runCatching {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        LocalTime.parse(dateStr, formatter)
    }.getOrNull() ?: LocalTime.now()

    /** yyyy년 MM월 */
    fun convertDateTimeFormat(date: LocalDate): String = kotlin.runCatching {
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월")
        date.format(formatter)
    }.getOrNull().orEmpty()

    fun convertTranslatorDateFormat(date: LocalDate): String = runCatching {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        formatter.format(date)
    }.getOrNull().orEmpty()

    /** 오늘을 포함한 주간 계산 */
    fun getStartOfWeek(today: LocalDate): LocalDate {
        return today.minusDays(today.dayOfWeek.value.toLong() % 7)
    }

    fun matchWeekdayInSameWeekUntilToday(
        from: LocalDate,
        to: LocalDate,
        today: LocalDate = LocalDate.now()
    ): LocalDate {
        val fromDayOfWeek = from.dayOfWeek.value % 7
        val toStartOfWeek = getStartOfWeek(to)
        Printer.e("LMH", "from $fromDayOfWeek startWeek $toStartOfWeek")
        val aligned = toStartOfWeek.plusDays((fromDayOfWeek).toLong())
        return if (aligned.isAfter(today)) today else aligned
    }

    fun formatTimeRange(startTimeStr: String, endTimeStr: String): String = kotlin.runCatching {
        val startTime = convertLocalTime(startTimeStr)
        val endTime = convertLocalTime(endTimeStr)
        val amPmFormatter = DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN)
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm")
        val startFormatted = startTime.format(amPmFormatter)
        val endFormatted = endTime.format(timeFormatter)
        "$startFormatted ~ $endFormatted"
    }.getOrNull().orEmpty()

    fun convertTimeBetweenDuration(startTime: String, endTime: String): String = runCatching {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        // 1. String → LocalTime 파싱
        val startLocalTime = LocalTime.parse(startTime, formatter)
        val endLocalTime = LocalTime.parse(endTime, formatter)

        // 2. Duration 계산
        val duration = Duration.between(startLocalTime, endLocalTime)

        // 만약 end 가 start보다 이전일 경우, 하루를 넘어서는 케이스 고려 (필요 시)
        val adjustedDuration = if (duration.isNegative) duration.plusDays(1) else duration

        // 3. 시간, 분 계산
        val totalMinutes = adjustedDuration.toMinutes()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        // 4. 포맷팅
        buildString {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0 || hours == 0L) append("${minutes}m")
        }.trim()
    }.getOrNull().orEmpty()
}