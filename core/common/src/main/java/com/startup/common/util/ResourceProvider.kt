package com.startup.common.util

interface ResourceProvider {
    fun getString(resId: Int): String
}
