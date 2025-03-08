package com.startup.home.step

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.startup.common.base.BaseActivity
import com.startup.common.base.NavigationEvent
import com.startup.common.base.UiEvent
import com.startup.common.util.OsVersions
import com.startup.home.StepCounterState
import com.startup.home.HomeViewModel
import com.startup.ui.WalkieTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow


@AndroidEntryPoint
class StepCounterActivity : BaseActivity<UiEvent, NavigationEvent>() {
    override val viewModel: HomeViewModel by viewModels()

    private val requiredPermissions = if (OsVersions.isGreaterThanOrEqualsTIRAMISU()) {
        arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
    } else if (OsVersions.isGreaterThanOrEqualsQ()) {
        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    } else {
        arrayOf()
    }

    override fun handleNavigationEvent(navigationEventFlow: Flow<NavigationEvent>) {
    }

    override fun handleUiEvent(uiEvent: UiEvent) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkBatteryOptimization()
        checkAndRequestPermissions()

        setContent {
            WalkieTheme {
                val currentState = viewModel.state as StepCounterState
                StepCounterScreen(
                    uiState = currentState,
                    resetStep = { viewModel.resetStepCount() }
                )
            }
        }
    }

    private fun checkBatteryOptimization() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            Intent().apply {
                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                data = Uri.parse("package:$packageName")
                startActivity(this)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (!hasRequiredPermissions()) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    startStepCounterService()
                }
            }.launch(requiredPermissions)
        } else {
            startStepCounterService()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startStepCounterService() {
        if (OsVersions.isGreaterThanOrEqualsO()) {
            viewModel.startCounting()
        } else {
            viewModel.stopCounting()
        }
    }
}

@Composable
fun StepCounterScreen(
    uiState: StepCounterState,
    modifier: Modifier = Modifier,
    resetStep: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "오늘의 걸음수",
            style = WalkieTheme.typography.head2,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "${uiState.steps}",
            style = WalkieTheme.typography.head1,
            color = WalkieTheme.colors.black
        )
        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = { resetStep.invoke() },
            colors = ButtonDefaults.buttonColors(
                containerColor = WalkieTheme.colors.blue300
            )
        ) {
            Text(text = "걸음수 초기화", style = WalkieTheme.typography.body2)
        }
    }
}