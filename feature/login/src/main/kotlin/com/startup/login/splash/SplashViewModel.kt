package com.startup.login.splash

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.startup.common.base.BaseViewModel
import com.startup.common.util.Printer
import com.startup.common.util.getLongVersionCode
import com.startup.domain.usecase.notification.GetAppMinVersionCode
import com.startup.domain.usecase.profile.GetMyData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getMyData: GetMyData,
    private val getAppMinVersionCode: GetAppMinVersionCode,
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _state = SplashViewStateImpl()
    override val state: SplashViewState get() = _state

    init {
        viewModelScope.launch {
            try {
                if (!_state.isForceUpdateDialogShow.value && isAppMinVersion()) {
                    _state.isForceUpdateDialogShow.update { true }
                } else {
                    getMyData.invoke(Unit).catch {
                        delay(2_000)
                        notifyEvent(SplashNavigationEvent.MoveToOnBoarding)
                    }.collect {
                        delay(2_000)
                        notifyEvent(SplashNavigationEvent.MoveToMainActivity)
                    }
                }
            } catch (e: Exception) {
                Printer.e("LMH", "CATCH $e")
            }
        }
    }

    private suspend fun isAppMinVersion(): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext runCatching {
                val minVersionCode = getAppMinVersionCode()
                val currentVersionCode = context.packageManager.getLongVersionCode(context.packageName)
                Printer.d("LMH", "minVersionCode: $minVersionCode, currentVersionCode: $currentVersionCode")
                currentVersionCode < minVersionCode
            }.getOrElse {
                Printer.e("LMH", "EXCEPTION $it")
                false
            }
        }
}