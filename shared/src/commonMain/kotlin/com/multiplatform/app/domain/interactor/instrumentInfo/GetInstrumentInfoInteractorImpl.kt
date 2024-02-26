package com.multiplatform.app.domain.interactor.instrumentInfo

import co.touchlab.kermit.Logger
import com.multiplatform.app.MR
import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.repositories.PaymentSdkRepository
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.di.baseLogger
import com.multiplatform.app.domain.models.ResourceResult
import com.multiplatform.app.platform.FileSystem
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.compose.readTextAsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive

class GetInstrumentInfoInteractorImpl(
    private val repository: PaymentSdkRepository,
    private val fileSystem: FileSystem,
    private val log: Logger,
): GetInstrumentInfoInteractor {

    override suspend fun invoke(request: InstrumentInfoRequest): Flow<ResourceResult<InstrumentInfoResponse>> = flow {
        log.d("Loading")
        emit(ResourceResult.Loading)
        runCatching {
            val stringJson = fileSystem.readText(MR.files.instrument_info_success)
            log.d("stringJson : $stringJson" )
            val mockResponse: InstrumentInfoResponse = Json.decodeFromString(stringJson)
            log.d("mockResponse : $mockResponse" )
            delay(1000)
            log.d("delay end " )
            emit(ResourceResult.Success(mockResponse))
            log.d("Success" )
        }.onFailure {
            emit(ResourceResult.Error(it))
            log.d("Error ${it.cause}" )
        }

        /*val config = RequestConfig<InstrumentInfoRequest>(
            headerMap = mapOf("" to ""),
            urlPath = "/api/sg/v1/pp-payment-ex/instrInfo",
            parameters = mapOf(
                "paymentToken" to request.paymentToken,
                "correlationId" to request.correlationId
            )
        )
        repository.instrumentInfo(config).onSuccess {
            emit(ResourceResult.Success(it))
        }.onFailure {
            emit(ResourceResult.Error(it))
        }*/
    }
}