package com.github.sdp.ratemyepfl.fragment.navigation

import androidx.fragment.app.Fragment

sealed class TabFragment(layout: Int): Fragment(layout) {
    abstract fun displayContent()

    companion object {

    }
}
