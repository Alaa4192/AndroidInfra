package com.infrastructure

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import com.infrastructure.extensions.*
import com.infrastructure.toolbar.ActionButton
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_base.view.*
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<T : BaseViewModel>(@LayoutRes private val layoutRes: Int) : DialogFragment(), BaseScreen {
    val viewModel by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }
    lateinit var swipeToRefresh: SwipeRefreshLayout

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    private fun getViewModelClass(): Class<T> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val baseView = inflater.inflate(R.layout.fragment_base, container, false) as ViewGroup
        val view = LayoutInflater.from(context).inflate(layoutRes, null)

        swipeToRefresh = baseView.findViewById(R.id.swipeToRefresh)
        swipeToRefresh.isEnabled = enableSwipeToRefresh()
        baseView.baseLayout.addView(view)

        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.showOrGone(showBackButton())
        back?.setOnClickListener { finish() }
        getColorDrawable()?.let {
            back?.imageTintMode = PorterDuff.Mode.SRC_IN
            back?.imageTintList = ColorStateList.valueOf(it)
        }

        initUi()
        initToolbarButtons()
        initViewModels()

        toolbarTitle.text = getName()
        loadIndicator.setProgressColor(getProgressColor())
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

    protected fun onBackButtonPressed() {
        finish()
    }

    abstract fun getName(): String

    abstract fun enableSwipeToRefresh(): Boolean

    abstract fun initUi()

    open fun getProgressColor(): Int = R.color.colorPrimary

    open fun initViewModels() {
        viewModel.uiStateViewModel.observe(viewLifecycleOwner, Observer {
            when (it) {
                UiState.SUCCESS -> onSuccess()
                UiState.LOADING -> onLoading()
                UiState.ERROR -> onError()
                else -> onSuccess()
            }
        })
    }

    open fun onSuccess() {
        loadIndicator.stop()
        progressContainer.gone()
    }

    open fun onLoading() {
        loadIndicator.start()
        progressContainer.show()
    }

    open fun onError() {
        loadIndicator.stop()
        progressContainer.gone()
    }

    open fun onDone() {
        loadIndicator.stop()
        progressContainer.gone()
    }

    open fun showBackButton(): Boolean = true

    open fun getActionButtons(): ArrayList<ActionButton>? = null

    open fun getColorDrawable(): Int? = null

    protected fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    protected fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE)
    }

    protected fun initToolbarButtons() {
        getActionButtons()?.forEach {
            when {
                it.iconRes != null -> {
                    val iv = AppCompatImageView(context)
                    iv.setImageResource(it.iconRes)
                    iv.tag = it.id
                    iv.background = getDrawable(R.drawable.ripple)
                    iv.adjustViewBounds = true
                    iv.setOnClickListener(it.callback)
                    iv.setPaddingDp(10)
                    actionBarButtonsContainer.addView(iv)
                }

                it.title != null -> {
                    val tv = AppCompatTextView(context)
                    tv.text = it.title
                    tv.tag = it.id
                    tv.background = getDrawable(R.drawable.ripple)
                    tv.setTextColor(Color.parseColor("#004A8E"))
                    tv.setOnClickListener(it.callback)
                    tv.setPaddingDp(5)
                    tv.setSidesMarginDp(10)

                    actionBarButtonsContainer.addView(tv)
                }
            }
        }
    }

    fun getDrawable(@DrawableRes res: Int) = ResourcesCompat.getDrawable(resources, res, null)

    fun finish() = activity?.finish()

    fun finishWithResult() {
        activity?.apply {
            setResult(RESULT_OK)
            finish()
        }
    }

    fun finishWithResult(data: Intent) {
        activity?.apply {
            setResult(RESULT_OK, data)
            finish()
        }
    }

    fun hideKeyboard(textInput: TextInputEditText) {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(textInput.windowToken, 0)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getSerializableExtra(key: String): T? {
        return (activity?.intent?.getSerializableExtra(key) as? T)
    }

    fun getString(key: String): String? {
        return activity?.intent?.getStringExtra(key)
    }
}

interface BaseScreen