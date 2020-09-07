package com.infrastructure

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_base.view.*
import java.lang.reflect.ParameterizedType

abstract class BaseBottomFragment<T : BaseViewModel>(@LayoutRes private val layoutRes: Int) : BottomSheetDialogFragment(), BaseDialog {
    val viewModel by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }

    private fun getViewModelClass(): Class<T> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val baseView = inflater.inflate(R.layout.dialog_base, container, false) as ViewGroup
        val view = LayoutInflater.from(context).inflate(layoutRes, null)

        baseView.baseLayout.addView(view)

        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModels()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackButtonPressed()
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }
        return dialog
    }

    fun onBackButtonPressed() {
        dismiss()
    }

    abstract fun initUi()

    abstract fun getName(): String

    open fun initViewModels() {
        viewModel.uiStateViewModel.observe(this, Observer {
            when (it) {
                UiState.SUCCESS -> onSuccess()
                UiState.LOADING -> onLoading()
                UiState.ERROR -> onError()
                UiState.DONE -> onDone()
                else -> onSuccess()
            }
        })
    }

    open fun onSuccess() {}

    open fun onLoading() {}

    open fun onError() {}

    open fun onDone() {}

}

interface BaseDialog