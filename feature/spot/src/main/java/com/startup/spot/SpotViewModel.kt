package com.startup.spot

import com.startup.common.base.BaseViewModel
import com.startup.common.base.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotViewModel @Inject constructor() : BaseViewModel() {
    override val state: State = object : State {}
}