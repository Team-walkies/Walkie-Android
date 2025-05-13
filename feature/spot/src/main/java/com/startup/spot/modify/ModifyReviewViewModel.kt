package com.startup.spot.modify

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseEvent
import com.startup.common.base.BaseState
import com.startup.common.base.BaseViewModel
import com.startup.common.util.ExtraConst
import com.startup.common.util.Printer
import com.startup.domain.model.spot.ModifyReviewWebPostRequest
import com.startup.domain.usecase.auth.GetToken
import com.startup.domain.usecase.auth.LocalLogout
import com.startup.spot.ModifyReviewEvent
import com.startup.spot.ModifyReviewNavigationEvent
import com.startup.spot.ModifyReviewUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ModifyReviewViewModel @Inject constructor(
    private val getToken: GetToken,
    private val stateHandle: SavedStateHandle,
    private val logout: LocalLogout,
) : BaseViewModel() {
    override val state: BaseState = object : BaseState {}

    private fun fetchLoadUrlParams() {
        getToken.invoke(Unit)
            .onEach {
                Printer.e("LMH", "GET $it")
                notifyEvent(
                    ModifyReviewEvent.LoadWebView(
                        ModifyReviewWebPostRequest(
                            reviewId = stateHandle.get<Int>(
                                ExtraConst.EXTRA_REVIEW_ID
                            ) ?: -1,
                            accessToken = it,
                            spotId = stateHandle.get<Int>(
                                ExtraConst.EXTRA_SPOT_ID
                            ) ?: -1
                        )
                    )
                )
            }
            .catch { Printer.e("LMH", "CATCH $it") }
            .launchIn(viewModelScope)
    }

    override fun handleViewModelEvent(event: BaseEvent) {
        if (event is ModifyReviewUiEvent) {
            when (event) {
                ModifyReviewUiEvent.FinishReviewModify -> {
                    notifyEvent(ModifyReviewNavigationEvent.FinishWithModifyActivity)
                }

                ModifyReviewUiEvent.FinishWebView -> {
                    notifyEvent(ModifyReviewNavigationEvent.Finish)
                }

                ModifyReviewUiEvent.LoadWebViewParams -> {
                    fetchLoadUrlParams()
                }
                ModifyReviewUiEvent.Logout -> {
                    logout
                        .invoke(Unit)
                        .onEach { notifyEvent(ModifyReviewNavigationEvent.Logout) }
                        .catch { }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}