package com.startup.ga


data class AnalyticsEvent(
    val eventName: String,
    val extras: List<Param> = emptyList(),
) {
    data class Param(val key: String, val value: String)
}