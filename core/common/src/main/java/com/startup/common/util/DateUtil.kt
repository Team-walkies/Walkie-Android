package com.startup.common.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {
    fun convertDateFormat(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())
        val date = inputFormat.parse(dateStr) ?: return ""
        return outputFormat.format(date)
    }
}