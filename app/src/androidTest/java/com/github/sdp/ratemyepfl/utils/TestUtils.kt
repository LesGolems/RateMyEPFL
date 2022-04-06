package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object TestUtils {
    fun getString(id: Int) = ApplicationProvider.getApplicationContext<Context>()
        .resources
        .getString(id)

    /**
     * Matches a view with the drawable [id]
     */
    fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with a drawable that has id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val expectedBitmap = view.context.getDrawable(id)?.toBitmap()
            return view is ImageView && view.drawable.toBitmap().sameAs(expectedBitmap)
        }
    }
}