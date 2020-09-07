package com.infrastructure.extensions

import android.animation.ObjectAnimator
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.addRotationExtension(isOpen: Boolean, animationDuration: Long, animationRotationAngel: Float) {
    val anim = if (!isOpen) {
        ObjectAnimator.ofFloat(this, "rotation", 0f, animationRotationAngel)
    } else {
        ObjectAnimator.ofFloat(this, "rotation", animationRotationAngel, 0f)
    }
    anim.duration = animationDuration
    anim.start()
}