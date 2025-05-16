package com.startup.common.extension

import java.text.NumberFormat

private val numberFormat: NumberFormat by lazy { NumberFormat.getNumberInstance() }

fun Int.formatWithLocale(): String = numberFormat.format(this)