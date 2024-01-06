package com.multiplatform.app.android

import android.app.Application
import com.multiplatform.app.di.appModule
import com.multiplatform.app.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@WeatherApp)
            modules(appModule)
        }
    }
}
