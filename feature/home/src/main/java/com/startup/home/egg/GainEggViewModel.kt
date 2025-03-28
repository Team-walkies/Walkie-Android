package com.startup.home.egg

import com.startup.common.base.BaseViewModel
import com.startup.common.util.ResponseErrorException
import com.startup.common.util.SessionExpireException
import com.startup.domain.usecase.GetGainEggList
import com.startup.home.egg.model.GainEggViewState
import com.startup.home.egg.model.GainEggViewStateImpl
import com.startup.home.egg.model.MyEggModel.Companion.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class GainEggViewModel @Inject constructor(
    getGainEggList: GetGainEggList,
) : BaseViewModel() {
    private val _state = GainEggViewStateImpl(
        getGainEggList.invoke(Unit)
            .map { it.toUiModel() }.catch {
                when (it) {
                    is ResponseErrorException -> {

                    }

                    is SessionExpireException -> {

                    }
                }
            }.stateInViewModel(emptyList())
    )
    override val state: GainEggViewState get() = _state
}