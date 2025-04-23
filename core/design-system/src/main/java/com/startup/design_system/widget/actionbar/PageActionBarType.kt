package com.startup.design_system.widget.actionbar

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color

@Keep
sealed class PageActionBarType(open val onBackClicked: () -> Unit) {
    data class JustBackActionBarType(override val onBackClicked: () -> Unit) :
        PageActionBarType(onBackClicked)

    data class TitleActionBarType(override val onBackClicked: () -> Unit, val title: String) :
        PageActionBarType(onBackClicked)

    data class TitleAndRightActionBarType(
        override val onBackClicked: () -> Unit,
        val title: String,
        val rightActionTitle: String,
        val rightActionClicked: () -> Unit
    ) : PageActionBarType(onBackClicked)

    data class TitleAndOptionalRightActionBarType(
        override val onBackClicked: () -> Unit,
        val title: String,
        val rightActionTitle: String,
        val enabled: Boolean,
        val enabledTextColor: Color,
        val disableTextColor: Color,
        val rightActionClicked: () -> Unit
    ) : PageActionBarType(onBackClicked)
}