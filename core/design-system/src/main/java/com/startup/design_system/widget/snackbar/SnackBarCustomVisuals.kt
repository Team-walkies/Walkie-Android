package com.startup.design_system.widget.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

data class SnackBarCustomVisuals(
    val annotatedMessage: AnnotatedString,
    val action: (() -> Unit)? = null,
    val actionLabelColor: Color? = null,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = true
) : SnackbarVisuals {
    override val message: String
        get() = annotatedMessage.text
}
