package com.github.sdp.ratemyepfl.utils

import android.widget.Toast

object FragmentUtils {

    fun displayOnToast(context: android.content.Context, message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}