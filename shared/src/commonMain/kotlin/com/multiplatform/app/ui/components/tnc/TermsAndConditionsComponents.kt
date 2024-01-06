package com.multiplatform.app.ui.components.tnc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.theme.prepaidIndigo
import com.multiplatform.app.ui.theme.prepaidYellow
import com.multiplatform.app.MR
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun tcScreenComposable(
    onAcceptTC: () -> Unit){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBarTnCComposable(scrollBehavior) },
        bottomBar = { bottomTnCComposable(onAcceptTC) },
        content = { contentTnCComposable(it) }
    )
}

fun LazyListScope.listItemText(stringResource: StringResource){
    item {
        bulletText(stringResource)
    }
}

@Composable
fun getAnnotatedString(stringResource: StringResource) : AnnotatedString = buildAnnotatedString {

}

@Composable
fun bulletText(stringResource: StringResource){
    Text(
        modifier = Modifier.padding(top = 10.dp, start = 20.dp),
        text = stringResource(stringResource),
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 28.sp,
        )
    )
}

internal fun LazyListScope.firstBulletPoints(){
    item {
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = stringResource(MR.strings.tc_title_one),
            style = MaterialTheme.typography.titleMedium
        )
    }
    listItemText(MR.strings.tc_bullet_point_1_1)
    listItemText(MR.strings.tc_bullet_point_1_2)
    listItemText(MR.strings.tc_bullet_point_1_3)
    listItemText(MR.strings.tc_bullet_point_1_4)
    listItemText(MR.strings.tc_bullet_point_1_5)
}

internal fun LazyListScope.secondBulletPoints(){
    item {
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = stringResource(MR.strings.tc_title_two),
            style = MaterialTheme.typography.titleMedium
        )
    }
    listItemText(MR.strings.tc_bullet_point_2_1)
    listItemText(MR.strings.tc_bullet_point_2_2)
    listItemText(MR.strings.tc_bullet_point_2_3)
}

@Composable
fun bottomTnCComposable(onAcceptTC : ()-> Unit) {
    Row (
        modifier = Modifier.fillMaxWidth().background(color = Color.White).shadow(elevation = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = prepaidIndigo),
            modifier = Modifier.fillMaxWidth().padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 10.dp),
            onClick = onAcceptTC){
            Text(modifier = Modifier, text = stringResource(MR.strings.tc_iagree), color = Color.Black,)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun contentTnCComposable(innerPaddingValues: PaddingValues) {
    LazyColumn(
        // consume insets as scaffold doesn't do it by default
        modifier = Modifier.consumeWindowInsets(innerPaddingValues).padding(24.dp),
        contentPadding = innerPaddingValues
    ) {
        item {
            Text(
                text = stringResource(MR.strings.tc_para_one),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                )
            )
        }
        firstBulletPoints()
        secondBulletPoints()
        item {
            Text( text = stringResource(MR.strings.tc_end_text),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarTnCComposable(scrollBehavior: TopAppBarScrollBehavior) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = prepaidYellow,
        ),
        title = {
            Text(
                color = Color.Black,
                text = stringResource(MR.strings.tc_title),
                maxLines = 1
            )
        },
        scrollBehavior = scrollBehavior,

        )
}