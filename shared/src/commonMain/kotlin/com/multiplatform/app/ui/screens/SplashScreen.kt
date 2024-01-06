package com.multiplatform.app.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.app.ui.theme.prepaidYellow
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit,
){
    LaunchedEffect(Unit) {
        delay(2000) // 2 seconds delay
        onNavigateNext()
    }

    Box (modifier = Modifier.fillMaxSize().background(color = prepaidYellow)){
        Box {
            Image(
                alignment = Alignment.TopEnd,
                modifier = Modifier.padding(top = 75.dp, start = 19.dp),
                painter = painterResource(MR.images.sixteen_dots_right_left),
                contentDescription = "Splash dots Graphics")
        }
        Box (modifier = Modifier.align(Alignment.TopEnd)) {
            Image(
                modifier = Modifier.padding(top = 50.dp),
                painter = painterResource(MR.images.sixteen_dots_right),
                contentDescription = "Splash dots Graphics")

        }

        Box (modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(MR.images.heya_logo),
                contentDescription = "Splash dots Graphics")

        }

        Box {
            Image(
                modifier = Modifier.padding(top = 310.dp),
                painter = painterResource(MR.images.cross_ninety_left),
                contentDescription = "Splash dots Graphics")

        }

        Box {
            Image(
                modifier = Modifier.padding(top = 390.dp, start = 50.dp),
                painter = painterResource(MR.images.cross_thirty_top),
                contentDescription = "Splash dots Graphics")

        }

        Box {
            Image(
                modifier = Modifier.padding(top = 390.dp, start = 50.dp),
                painter = painterResource(MR.images.cross_thirty_top),
                contentDescription = "Splash dots Graphics")

        }

        Image(
            modifier = Modifier.padding(bottom = 100.dp).align(Alignment.BottomEnd),
            painter = painterResource(MR.images.slashes_bottom_right),
            contentDescription = "Splash dots Graphics")


        Image(
            modifier = Modifier.padding(top = 240.dp, start = 50.dp).align(Alignment.Center),
            painter = painterResource(MR.images.cross_bottom),
            contentDescription = "Splash dots Graphics")

        Image(
            modifier = Modifier.padding(bottom = 150.dp, start = 0.dp).align(Alignment.BottomStart),
            painter = painterResource(MR.images.sixteen_dots_right_bottom),
            contentDescription = "Splash dots Graphics")

    }
}

