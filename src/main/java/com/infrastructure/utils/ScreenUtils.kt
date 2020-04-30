package com.infrastructure.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.infrastructure.BaseActivity
import com.infrastructure.BaseDialog
import com.infrastructure.BaseScreen
import com.infrastructure.R

object ScreenUtils {

    fun openScreen(fragment: Class<out BaseScreen>, fragmentActivity: FragmentActivity,
                   clearStack: Boolean = false, bundle: Bundle? = null,
                   requestCode: Int? = null) {
        val intent = Intent(fragmentActivity, BaseActivity::class.java)

        buildIntent(clearStack, intent, bundle, fragment)

        requestCode?.let {
            fragmentActivity.startActivityForResult(intent, it)
        } ?: run {
            fragmentActivity.startActivity(intent)
        }
    }

    fun openScreen(fragment: Class<out BaseScreen>, baseActivity: AppCompatActivity, clearStack: Boolean) {
        val intent = Intent(baseActivity, BaseActivity::class.java)

        buildIntent(clearStack, intent, null, fragment)

        baseActivity.startActivity(intent)
    }

    fun openDialog(fragment: Class<out BaseDialog>, fragmentActivity: FragmentActivity, bundle: Bundle? = null) {
        val bottomSheetDialogFragment = fragment.newInstance() as BottomSheetDialogFragment
        bottomSheetDialogFragment.arguments = bundle

        val beginTransaction = fragmentActivity.supportFragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)

        bottomSheetDialogFragment.show(beginTransaction, fragment.simpleName)
    }

    private fun buildIntent(clearStack: Boolean, intent: Intent, bundle: Bundle?, fragment: Class<out BaseScreen>) {
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val newBundle = bundle ?: Bundle()
        newBundle.putString("frag", fragment.name)
        intent.putExtras(newBundle)
    }
}