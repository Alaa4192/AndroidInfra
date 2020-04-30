package com.infrastructure

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class BaseActivity : AppCompatActivity() {

    private var dialogFragment: DialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        intent?.extras?.let {
            it.getString("frag")?.let { fragmentName ->
                val screenClass = (Class.forName(fragmentName) as Class<out BaseScreen>)
                dialogFragment = screenClass.newInstance() as DialogFragment
                dialogFragment?.show(supportFragmentManager, fragmentName)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialogFragment?.onActivityResult(requestCode, resultCode, data)
    }

}