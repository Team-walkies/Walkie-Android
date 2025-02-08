package com.startup.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object WalkieTheme {

    val colors: WalkieSemanticColor
        @Composable
        @ReadOnlyComposable
        get() = LocalWalkieColors.current

    val typography: WalkieTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalWalkieTypography.current
}

@Composable
fun WalkieTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWalkieColors provides if (darkTheme) LocalWalkieDarkColorScheme else LocalWalkieLightColorScheme,
        LocalWalkieTypography provides getWalkieTypography(),
    ) {
        MaterialTheme(content = content)
    }
}
