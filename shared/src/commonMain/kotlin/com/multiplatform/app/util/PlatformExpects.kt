package com.multiplatform.app.util

expect class PlatformExpects() {
    fun setStatusBarColor()
    fun getModelName(): String
    fun getOSVersion(): String
    fun getPlatform(): String
    fun getManufacturer(): String

}