package com.startup.common.extension

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true

fun Byte?.orZero() = this ?: 0

fun Short?.orZero() = this ?: 0

fun Int?.orZero() = this ?: 0

fun Long?.orZero() = this ?: 0L

fun Float?.orZero() = this ?: 0.0F

fun Double?.orZero() = this ?: 0.0

fun Char?.orBlank() = this ?: '\u0000'

fun String?.orBlank() = this ?: ""

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

inline fun <reified T : Enum<T>> safeEnumOf(type: String?, default: T): T =
    runCatching {
        java.lang.Enum.valueOf(T::class.java, type)
    }.getOrDefault(default)
