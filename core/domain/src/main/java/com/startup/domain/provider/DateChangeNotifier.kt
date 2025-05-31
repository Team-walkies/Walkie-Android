package com.startup.domain.provider

interface DateChangeNotifier {
    fun setListener(listener: DateChangeListener)
    fun removeListener()
    fun startListening()
    fun stopListening()
}