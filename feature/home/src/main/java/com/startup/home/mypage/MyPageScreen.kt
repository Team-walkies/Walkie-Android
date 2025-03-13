package com.startup.home.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.startup.home.HomeScreenNavigationEvent
import com.startup.home.MyPageScreenNavigationEvent

@Composable
fun MyPageScreen(onNavigationEvent: (MyPageScreenNavigationEvent)-> Unit){
    Column {
        Text(text = "MyPageScreen")
    }
}