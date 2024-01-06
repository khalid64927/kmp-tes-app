package com.multiplatform.app.models.uimodels

import dev.icerock.moko.resources.ImageResource

abstract class AppBarConfig {
    open var leftIcon: IconConfig = IconConfig()
    open var title: String = ""
    open var showAppBar: Boolean = true
}

data class IconConfig(
    var showIcon: Boolean = false,
    var imageResource: ImageResource? = null,
    val action: () -> Unit = {}
)

class NoAppBar: AppBarConfig() {
    override var showAppBar: Boolean = false
}

class LoginAppBar(onBackAction: ()-> Unit): AppBarConfig(){
    override var leftIcon = IconConfig(showIcon = true, )
}

//val noAppBar = AppBarUi(leftIcon = IconConfig(), rightIcon = IconConfig())

