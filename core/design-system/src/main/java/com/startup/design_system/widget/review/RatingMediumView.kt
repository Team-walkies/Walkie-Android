package com.startup.design_system.widget.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.startup.design_system.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable

@Composable
fun RatingMediumView(
    modifier: Modifier = Modifier,
    rating: Int,
    onClickRating: ((Int) -> Unit)? = null
) {
    val ratingScore = rating.takeIf { it in 0..5 } ?: 0
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(5) {
            Box(modifier = Modifier.noRippleClickable {
                onClickRating?.invoke(it + 1)
            }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_star), contentDescription = null,
                    tint = WalkieTheme.colors.gray300
                )
                if (it + 1 <= ratingScore) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_star), contentDescription = null,
                        tint = WalkieTheme.colors.blue300
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun PreviewRatingMediumView() {
    WalkieTheme {
        RatingMediumView(rating = 4)
    }
}