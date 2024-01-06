package com.multiplatform.app.common

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

open class BaseViewModel: ViewModel(), KoinComponent {

    protected fun launchAsync(block: suspend ()->Unit): Job{
        return viewModelScope.launch (
            CoroutineExceptionHandler { coroutineContext, throwable ->
                // do error handling
            }
        ) {
            block()
        }
    }
}