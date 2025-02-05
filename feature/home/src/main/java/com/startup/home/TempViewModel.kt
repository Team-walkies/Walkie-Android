package com.startup.home

import com.startup.common.base.BaseViewModel
import com.startup.common.base.State
import com.startup.domain.usecase.TempUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TempViewModel @Inject constructor(private val tempUseCase: TempUseCase) : BaseViewModel() {
    override val state: State
        get() = object : State{}



}