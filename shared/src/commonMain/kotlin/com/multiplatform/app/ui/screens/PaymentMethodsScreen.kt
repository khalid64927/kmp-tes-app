package com.multiplatform.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.app.di.baseLogger
import com.multiplatform.app.domain.models.ResourceResult
import com.multiplatform.app.ui.components.bottomsheet.CenteredLoadingComponent
import com.multiplatform.app.ui.components.core.ComponentRectangleLineLong
import com.multiplatform.app.ui.components.core.ComponentRectangleLineShort
import com.multiplatform.app.ui.components.core.ComponentSquare
import com.multiplatform.app.ui.designsystem.components.HyperlinkText
import com.multiplatform.app.ui.designsystem.components.LinkType
import com.multiplatform.app.ui.designsystem.components.PaymentItemRow
import com.multiplatform.app.viewmodels.PAYMENT_OPTIONS
import com.multiplatform.app.viewmodels.PaymentMethodEvent
import com.multiplatform.app.viewmodels.PaymentMethodsViewModel
import com.multiplatform.app.viewmodels.SheetData
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodsScreen(
    onClick: ()-> Unit = { }
){
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val viewModel = getViewModel(
        key = "payments-screen",
        factory = viewModelFactory { PaymentMethodsViewModel() }
    )
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show bottom sheet") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showBottomSheet = true
                }
            )
        }
    ) {
        // Scaffold content
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {

                    when(state.instrumentInfoApiState) {
                        is ResourceResult.Loading -> CenteredLoadingComponent()
                        is ResourceResult.Error -> ErrorPage()
                        is ResourceResult.Success -> PaymentMethodsComposable(
                            state.sheetDataList, onClick = { token, linkType ->
                                if(linkType == LinkType.JSON) {
                                    runCatching {
                                        val hyperlinkTokenData: HyperlinkTokenData = Json.decodeFromString(token)
                                        viewModel.onEvent(
                                            PaymentMethodEvent.RemoveSavedCard(
                                                intrumentId = hyperlinkTokenData.instrId,
                                                instrument = "CC",
                                                paymentOptions = PAYMENT_OPTIONS.VISA_CARD)
                                        )

                                    }.onFailure {
                                            viewModel.onEvent(PaymentMethodEvent.LogEvent(it.message.toString()))
                                    }
                                }
                            })
                        else -> {}
                    }

                }
            }
        }
    }
}

@Composable
fun ErrorPage(){
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(top = 80.dp, bottom = 80.dp),
    ) {


    }
}

@Composable
fun PaymentMethodsComposable(
    sheetDataList: List<SheetData>,
    onClick: (token: String, linkType: LinkType)->Unit = { _, _ -> }){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        HyperlinkText("[Remove Card](www.google.com)", onClick = {token, linkType -> println(" linkType : $linkType, token: $token") })
        sheetDataList.forEach {
            PaymentItemRow(it, onClick)
        }
    }
}

@Serializable
data class HyperlinkTokenData(val instrument: String, val instrId: String)




