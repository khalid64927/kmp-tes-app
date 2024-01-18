package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun TopNavigation(topNavConfig : TopNavigationConfig){
    Row(modifier = Modifier.fillMaxWidth().shadow(elevation = 1.dp)) {
        Box(
            modifier = Modifier.
            fillMaxWidth().
            size(height = 60.dp, width = 0.dp).
            padding(10.dp),
        ){
            topNavConfig.leftImageResource?.run {
                Image(
                    modifier = Modifier.
                    size(width = 40.dp, height = 40.dp).
                    padding(PaddingValues(start = 0.dp)).
                    clickable {
                        topNavConfig.onLeftClick()
                    }.align(Alignment.CenterStart),
                    painter = painterResource(this),
                    contentDescription = "Splash dots Graphics")
            }

            Column(modifier = Modifier.align(Alignment.Center)) {
                TitleTextBold(
                    modifier = Modifier,
                    string = stringResource(topNavConfig.stringResource))
            }

            topNavConfig.rightImageResource?.run {
                Image(
                    modifier = Modifier.size(width = 40.dp, height = 40.dp).
                    padding(PaddingValues(end = 0.dp)).
                    clickable { topNavConfig.onRightClick() }.align(Alignment.CenterEnd),
                    painter = painterResource(this),
                    contentDescription = "Splash dots Graphics")

            }
        }
    }

}

data class TopNavigationConfig(
    val stringResource: StringResource,
    val leftImageResource: ImageResource? = null,
    val rightImageResource: ImageResource? = null,
    val onLeftClick : ()-> Unit = {},
    val onRightClick : ()-> Unit = {}
)