package com.multiplatform.app.di

fun initKoin() {
    initKoin {
        modules(appModule)
    }
}
