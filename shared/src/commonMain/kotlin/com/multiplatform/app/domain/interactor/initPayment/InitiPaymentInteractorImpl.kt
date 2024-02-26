package com.multiplatform.app.domain.interactor.initPayment

import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.repositories.PaymentSdkRepository
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InitiPaymentInteractorImpl(
    private val repository: PaymentSdkRepository
): InitiPaymentInteractor {

    override suspend fun invoke(request: InstrumentInfoRequest): Flow<ResourceResult<InstrumentInfoResponse>> = flow {
        val config = RequestConfig<InstrumentInfoRequest>(
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
        }
    }
}