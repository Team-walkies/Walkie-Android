package com.startup.ga

fun AnalyticsHelper.logEvent(eventName:String) {
    logEvent(
        AnalyticsEvent(
            eventName = eventName,
            extras = emptyList(),
        ),
    )
}
