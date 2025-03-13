package com.startup.spot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.startup.ui.WalkieTheme

@Composable
fun SpotScreen() {
    WalkieTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(WalkieTheme.colors.white)) {
            Text(text = "SpotScreen")
        }
    }
}