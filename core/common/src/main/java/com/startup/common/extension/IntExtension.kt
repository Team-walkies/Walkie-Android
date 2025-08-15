package com.startup.common.extension

import java.text.NumberFormat

private val numberFormat: NumberFormat by lazy { NumberFormat.getNumberInstance() }

fun Int.formatWithLocale(): String = numberFormat.format(this)

fun Int.formatNumber(): String {
    val formatter = NumberFormat.getNumberInstance()
    return formatter.format(this)
}