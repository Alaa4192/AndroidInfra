package com.infrastructure.extensions

import java.text.DecimalFormat

fun String?.getString(): String {
    return this?.apply {
        toString()
    } ?: run {
        ""
    }
}

fun String.includesCharacter(char: Char): Boolean {
    return this.contains(char)
}

fun Float?.float(): Float {
    return this?.apply {} ?: run { 0f }
}

fun Float.toStringTwoDigits(): String {
    val format = DecimalFormat.getInstance()
    format.maximumFractionDigits = 2
    format.minimumFractionDigits = 0
    return format.format(this)
}

fun Float.addCurrency(): String {
    return "${this.toStringTwoDigits()}₪"
}