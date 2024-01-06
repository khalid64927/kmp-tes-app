package com.multiplatform.app.data.remote.models

enum class RemoteExceptionType {
    SERVER_ERROR,
    CLIENT_ERROR,
    PARSING_ERROR,
    UNKNOWN,
}

class RemoteException(val type: RemoteExceptionType) : Exception()
