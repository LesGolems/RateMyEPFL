package com.github.sdp.ratemyepfl.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider

object TestUtils {
    fun getString(id: Int) = ApplicationProvider.getApplicationContext<Context>()
        .resources
        .getString(id)
}