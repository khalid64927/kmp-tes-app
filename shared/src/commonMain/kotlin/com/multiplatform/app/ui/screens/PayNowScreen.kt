package com.multiplatform.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.app.MR
import com.multiplatform.app.ui.designsystem.components.Body2BoldText
import com.multiplatform.app.ui.designsystem.components.Body2Text
import com.multiplatform.app.ui.designsystem.components.TopNavigation
import com.multiplatform.app.ui.designsystem.components.TopNavigationConfig

@Composable
fun PayNowScreen(
    onNavigateToSearch: () -> Unit = {},
    modifier: Modifier = Modifier,
){
    Column(modifier = Modifier.fillMaxSize()) {
        TopNavigation(
            TopNavigationConfig(stringResource = MR.strings.paynow_title)
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row (modifier = Modifier.fillMaxWidth().background(Color(0xFFF7F7F7)).padding(
                PaddingValues(top = 16.dp, bottom = 16.dp)
            )){
                Body2Text(modifier = Modifier.padding(PaddingValues(end = 5.dp)), stringResource = MR.strings.paynow_timer)
                Body2BoldText(stringResource = MR.strings.paynow_timer_min)
            }
        }
    }
}

