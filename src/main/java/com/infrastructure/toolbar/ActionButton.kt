package com.infrastructure.toolbar

import android.view.View
import androidx.annotation.DrawableRes

data class ActionButton(
        val id: String,
        val title: String? = null,
        @DrawableRes val iconRes: Int? = null,
        val callback: View.OnClickListener
)