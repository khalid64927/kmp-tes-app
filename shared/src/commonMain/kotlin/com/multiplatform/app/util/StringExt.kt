package com.multiplatform.app.util


fun String.isDigitsOnly() = try {
    toInt()
    true
} catch (e: NumberFormatException) {
    false
}

