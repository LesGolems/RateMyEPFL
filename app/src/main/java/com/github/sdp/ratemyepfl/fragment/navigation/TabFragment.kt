package com.github.sdp.ratemyepfl.fragment.navigation

import android.widget.Button
import androidx.fragment.app.Fragment

sealed class TabFragment(layout: Int): Fragment(layout) {
    abstract fun displayContent()

    protected fun setListeners(button: Button) {
        button.setOnClickListener {
            displayContent()
        }
    }
}
