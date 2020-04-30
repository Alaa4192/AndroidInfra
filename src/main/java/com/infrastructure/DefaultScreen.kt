package com.infrastructure

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

abstract class DefaultScreen<T : BaseViewModel>(@LayoutRes private val layoutRes: Int) : BaseFragment<T>(layoutRes) {

    override fun getTheme(): Int = R.style.default_screen

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnDismissListener {
            finish()
        }

        dialog?.setOnCancelListener {
            finish()
        }
    }

    override fun enableSwipeToRefresh(): Boolean = false
}