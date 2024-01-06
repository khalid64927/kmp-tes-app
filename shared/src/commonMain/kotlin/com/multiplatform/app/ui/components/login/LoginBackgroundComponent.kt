package com.multiplatform.app.ui.components.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.theme.prepaidGreen
import com.multiplatform.app.ui.theme.prepaidIndigo
import com.multiplatform.app.ui.theme.prepaidYellow
import com.multiplatform.app.viewmodels.LoginButtonUiState
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource


@Composable
fun LoginBackgroundComponent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit){
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.7f).fillMaxSize().background(color = prepaidYellow)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.padding(top = 88.dp, start = 83.dp),
                    painter = painterResource(MR.images.cross_thirty_top),
                    contentDescription = "Splash dots Graphics")
                Image(
                    modifier = Modifier.padding(top = 53.dp, start = 24.dp),
                    painter = painterResource(MR.images.cross_bottom),
                    contentDescription = "Splash dots Graphics")
                Image(
                    modifier = Modifier.
                    align(Alignment.Center).
                    size(width = 88.dp, height = 75.dp),
                    painter = painterResource(MR.images.heya_logo),
                    contentDescription = "Splash dots Graphics")

                Image(
                    modifier = Modifier.padding(bottom = 0.dp).align(Alignment.BottomEnd).size(width = 12.dp, height = 103.dp),
                    painter = painterResource(MR.images.slashes_bottom_right),
                    contentDescription = "Splash dots Graphics")

                Image(
                    modifier = Modifier.padding(top = 41.dp, end = 57.dp).align(Alignment.TopEnd).size(width = 40.dp, height = 40.dp),
                    painter = painterResource(MR.images.cross_thirty_top),
                    contentDescription = "Splash dots Graphics")
            }

        }

        Row (modifier = Modifier.weight(2.3f).fillMaxSize().background(color = Color.White)){
            Column (modifier = Modifier.fillMaxSize().padding(top = 32.dp, start = 24.dp, end = 24.dp)){
                Text(
                    modifier = Modifier,
                    text = stringResource(MR.strings.login_title),
                    style = TextStyle(
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        color = Color.Black,
                        letterSpacing = 0.02.sp,
                    )
                )
                content()
            }
        }
    }
}


@Composable
fun smallLoadingIndicator(){
    CircularProgressIndicator(
        modifier = Modifier.size(width = 23.dp, height = 23.dp),
        color = Color.Black,
        strokeWidth = 2.dp,
        trackColor = prepaidIndigo,
    )

}

@Composable
fun ColumnScope.loginButtonComponent(
    loginButtonUiState: LoginButtonUiState,
    modifier: Modifier,
    onClick: ()->Unit
){
    val color = if(loginButtonUiState.isSuccess) prepaidGreen else prepaidIndigo
    Row(modifier = modifier) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = color
            ),
            modifier = Modifier.fillMaxWidth().padding(
                top = 32.dp,
                bottom = 10.dp).clickable(enabled = loginButtonUiState.allowClick()) {  },
            onClick = { if(!(loginButtonUiState.isSuccess || loginButtonUiState.isLoading)) onClick() }){
            if(loginButtonUiState.isLoading){
                smallLoadingIndicator()
            } else if(loginButtonUiState.isSuccess){
                Image(
                    modifier = Modifier.size(width = 21.dp, height = 15.dp),
                    painter = painterResource(MR.images.white_tick),
                    contentDescription = "Login tick mark")
            } else {
                // initial or failure state
                Text(modifier = Modifier, text = stringResource(MR.strings.login_iagree) , color = Color.Black,)

            }
        }
    }

}

