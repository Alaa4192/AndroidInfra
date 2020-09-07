package com.infrastructure.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.text(): String {
    return this.text?.toString() ?: ""
}

fun TextInputEditText.double(): Double = this.text.double()

fun AppCompatAutoCompleteTextView.text(): String {
    return this.text?.toString() ?: ""
}

fun View.dp(size: Int): Float {
    val scale = context.resources.displayMetrics.density
    return (size * scale + 0.5f).toFloat()
}

fun View.setPaddingDp(padding: Int) {
    val scale = context.resources.displayMetrics.density
    val dpPadding = (padding * scale + 0.5f).toInt()
    this.setPadding(dpPadding, dpPadding, dpPadding, dpPadding)
}

fun View.setSidesMarginDp(margin: Int, params: ViewGroup.LayoutParams? = null) {
    val scale = context.resources.displayMetrics.density
    val dpMargin = (margin * scale + 0.5f).toInt()

    val newParams = params?.let {
        (it as LinearLayout.LayoutParams).apply {
            setMargins(dpMargin, 0, dpMargin, 0)
        }
    } ?: run {
        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(dpMargin, 0, dpMargin, 0)
        }
    }

    this.layoutParams = newParams
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.showOrGone(isVisible: Boolean) {
    visibility =
            if (isVisible)
                View.VISIBLE
            else
                View.GONE
}

fun View.showOrHide(isVisible: Boolean) {
    visibility =
            if (isVisible)
                View.VISIBLE
            else
                View.INVISIBLE
}

fun RecyclerView.configureVertically(context: Context?) {
    layoutManager = LinearLayoutManager(context)
}

fun RecyclerView.configureHorizontally(context: Context?) {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
}

fun RecyclerView.configureGrid(context: Context?, span: Int) {
    layoutManager = GridLayoutManager(context, span)
}

fun RecyclerView.configDivider() {
    val dividerItemDecoration = DividerItemDecoration(this.context, (this.layoutManager as LinearLayoutManager).orientation)
    addItemDecoration(dividerItemDecoration)
}