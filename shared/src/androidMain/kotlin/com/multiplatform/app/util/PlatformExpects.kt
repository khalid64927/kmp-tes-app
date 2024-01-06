package com.multiplatform.app.util

import android.os.Build

actual class PlatformExpects {
    actual fun setStatusBarColor() {
        TODO("Not yet implemented")
    }

    actual fun getModelName() = "Pixel 8"/*android.os.Build.MODEL*/

    actual fun getOSVersion() = Build.VERSION.RELEASE

    actual fun getPlatform() = "Android"

    actual fun getManufacturer() = "Google"
}