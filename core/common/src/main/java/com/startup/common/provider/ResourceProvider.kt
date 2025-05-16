package com.startup.common.provider

interface ResourceProvider {
    fun getString(resId: Int): String
}
