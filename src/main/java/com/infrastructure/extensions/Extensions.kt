package com.infrastructure.extensions

import android.text.Editable
import java.text.DecimalFormat

fun Editable?.double(): Double {
    return if (!this.isNullOrEmpty()) {
        this.toString().toDouble()
    } else {
        0.0
    }
}

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

fun String?.float(): Float {
    return this?.toFloat() ?: 0f
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

fun Float.toStringOneDigit(): String {
    val format = DecimalFormat.getInstance()
    format.maximumFractionDigits = 1
    format.minimumFractionDigits = 0
    return format.format(this)
}

fun Float.addCurrency(): String {
    return "${this.toStringTwoDigits()}₪"
}

fun <E> List<E>.toArrayList(): ArrayList<E> {
    return ArrayList<E>(this)
}

fun <E> ArrayList<E>?.print(): String {
    return this?.let {
        var list = ""
        it.forEachIndexed { index, e ->
            list += e.toString()
            if (index < it.size - 1) {
                list += ", "
            }
        }
        list
    } ?: run { "" }
}

fun <E> ArrayList<E>?.addIfNotFound(item: E) {
    if (this?.contains(item) == false) {
        this.add(item)
    }
}

fun Double.roundToTwoDigits(): Double {
    return (this * 100).toInt() / 100.0
}

fun <E> MutableCollection<E>.addIf(e: E, shouldAdd: () -> Boolean) {
    if (shouldAdd()) {
        this.add(e)
    }
}