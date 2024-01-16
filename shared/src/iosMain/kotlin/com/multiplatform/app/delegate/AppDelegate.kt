package com.multiplatform.app.delegate

import platform.Foundation.NSNotification
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationWillEnterForegroundNotification


// TODO: test if this works
class AppDelegate {
    var isInBackground = false

    init {
        registerAppLifecycleNotifications()
    }

    private fun registerAppLifecycleNotifications() {
        val notificationCenter = NSNotificationCenter.defaultCenter()

        notificationCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = null
        ) { _ ->
            // Application entered background
            isInBackground = true
        }

        notificationCenter.addObserverForName(
            name = UIApplicationWillEnterForegroundNotification,
            `object` = null,
            queue = null
        ) { _ ->
            // Application will enter foreground
            isInBackground = false
        }
    }

    @Suppress("unused")
    private fun applicationDidEnterBackground(notification: NSNotification) {
        // Application entered background
        isInBackground = true
    }

    @Suppress("unused")
    private fun applicationWillEnterForeground(notification: NSNotification) {
        // Application will enter foreground
        isInBackground = false
    }

}