package com.multiplatform.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.app.MR
import com.multiplatform.app.ui.components.core.NumberedListItem
import com.multiplatform.app.ui.designsystem.components.Body2BoldText
import com.multiplatform.app.ui.designsystem.components.Body2Text
import com.multiplatform.app.ui.designsystem.components.FilledButtonBlack
import com.multiplatform.app.ui.designsystem.components.HyperlinkText
import com.multiplatform.app.ui.designsystem.components.QrCodeComponent
import com.multiplatform.app.ui.designsystem.components.SpanTypographyTokens
import com.multiplatform.app.ui.designsystem.components.TitleTextBold
import com.multiplatform.app.ui.designsystem.components.TitleTextRegular2
import com.multiplatform.app.ui.designsystem.components.TopNavigation
import com.multiplatform.app.ui.designsystem.components.TopNavigationConfig
import com.multiplatform.app.ui.theme.MonoColors
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PayNowScreen(
    onNavigateToSearch: () -> Unit = {},
    modifier: Modifier = Modifier,
){
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().background(MonoColors.white).verticalScroll(state = scrollState)) {
        TopNavigation(
            TopNavigationConfig(stringResource = MR.strings.paynow_title)
        )
        timerComponent()
        Body2Text(
            modifier = Modifier.
            padding(PaddingValues(start = 24.dp, top = 24.dp)),
            string = stringResource(MR.strings.paynow_ref, "123233")
        )
        Spacer(modifier = Modifier.size(height = 24.dp, width = 0.dp).fillMaxWidth())
        TitleTextBold(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            string = stringResource(MR.strings.paynow_amount, "$380"))
        Spacer(modifier = Modifier.size(height = 24.dp, width = 0.dp).fillMaxWidth())
        QrCodeComponent(
            modifier = Modifier.
                size(180.dp).
                align(Alignment.CenterHorizontally),data = "Hello World")
        Spacer(modifier = Modifier.size(height = 16.dp, width = 0.dp).fillMaxWidth())
        FilledButtonBlack(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { /*TODO*/},
            text = stringResource(MR.strings.paynow_save_qr_code_button)
        )
        Divider(
            modifier = Modifier.
                fillMaxWidth().
                padding(PaddingValues(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)).
                size(width = 0.dp, height = 1.dp),
            color = MonoColors.lightGrey)

        instructionComponent()

        TitleTextBold(
            modifier = Modifier.padding(PaddingValues(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 0.dp)),
            string = stringResource(MR.strings.paynow_back_title))

        HyperlinkText(
            onClick = { token, lintType ->
                      println("token $token linktype $lintType")
            },
            spanStyle = SpanTypographyTokens.body1,
            modifier =
                Modifier.padding(
                    PaddingValues(
                        top = 8.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 10.dp)),
            string = stringResource(MR.strings.paynow_back_body)
        )
    }
}

/**
 * timer component
 * TODO: add parameters
 * */
@Composable
fun ColumnScope.timerComponent(){
    Row (
        modifier = Modifier.
        fillMaxWidth().
        background(MonoColors.lightBg1).
        padding(16.dp)) {

        Body2Text(
            modifier = Modifier.
            padding(PaddingValues(end = 5.dp)),
            string = stringResource(MR.strings.paynow_timer, "10:40") )

        Body2BoldText(
            string = stringResource(MR.strings.paynow_timer_minutes))
    }

}

@Composable
fun ColumnScope.instructionComponent(){
    TitleTextRegular2(
        modifier = Modifier.padding(PaddingValues(start = 24.dp, end = 24.dp)),
        string = stringResource(MR.strings.paynow_instruction_title))

    Spacer(modifier = Modifier.size(height = 8.dp, width = 0.dp).fillMaxWidth())

    NumberedListItem(
        onClick = { token, lintType ->
            println("token $token linktype $lintType")
        },
        modifier = Modifier.padding(PaddingValues(start = 5.dp)),
        number = "1.",
        pointString = stringResource(MR.strings.paynow_instruction_1))
    NumberedListItem(
        onClick = { token, lintType ->
            println("token $token linktype $lintType")
        },
        modifier = Modifier.padding(PaddingValues(start = 5.dp)),
        number = "2.",
        pointString = stringResource(MR.strings.paynow_instruction_2))

}

