package com.startup.home.egg.model

import com.startup.common.base.BaseState
import kotlinx.coroutines.flow.StateFlow

class GainEggViewStateImpl(override val eggList: StateFlow<List<MyEggModel>>) : GainEggViewState {
}

interface GainEggViewState: BaseState {
    val eggList: StateFlow<List<MyEggModel>>
}