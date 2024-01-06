package com.multiplatform.app.util

import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice
actual class PlatformExpects {
    actual fun setStatusBarColor() {
        //no-op
    }

    actual fun getModelName(): String {
        return NSProcessInfo.processInfo.hostName
    }

    actual fun getOSVersion(): String {
        return UIDevice.currentDevice.systemVersion
    }

    actual fun getPlatform() = "iOS"

    actual fun getManufacturer() = "Apple"

}