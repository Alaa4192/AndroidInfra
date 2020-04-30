package com.infrastructure

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

abstract class BottomScreen<T : BaseViewModel>(@LayoutRes private val layoutRes: Int) : BaseBottomFragment<T>(layoutRes) {

    override fun getTheme(): Int = R.style.bottom_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnDismissListener {

        }

        dialog?.setOnCancelListener {

        }
    }
}