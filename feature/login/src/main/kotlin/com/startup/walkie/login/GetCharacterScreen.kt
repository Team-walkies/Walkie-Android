package com.startup.walkie.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.startup.login.R
import com.startup.ui.WalkieTheme
import com.startup.ui.noRippleClickable
import com.startup.walkie.login.model.GetCharacterNavigationEvent

@Composable
fun GetCharacterScreen(nickName: String, onNavigationEvent: (GetCharacterNavigationEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = WalkieTheme.colors.white)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = stringResource(R.string.get_character_introduce, nickName),
            style = WalkieTheme.typography.head2.copy(color = WalkieTheme.colors.gray700),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.get_character_introduce_sub_title, "해파리"),
            style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.blue400)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.jelly_1),
            contentDescription = stringResource(R.string.desc_character)
        )
        Spacer(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier
                .noRippleClickable {
                    onNavigationEvent.invoke(GetCharacterNavigationEvent.MoveToMainActivity)
                }
                .padding(bottom = 28.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WalkieTheme.colors.blue300,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 15.dp),
                text = stringResource(R.string.get_character_action_button, "해파리"),
                style = WalkieTheme.typography.body1.copy(color = WalkieTheme.colors.white),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
@PreviewScreenSizes
fun PreviewGetCharacterScreen() {
    WalkieTheme {
        GetCharacterScreen(nickName = "승빈짱짱") {}
    }
}