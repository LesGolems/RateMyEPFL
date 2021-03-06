package com.github.sdp.ratemyepfl.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object TestUtils {
    fun getString(id: Int) = ApplicationProvider.getApplicationContext<Context>().resources
        .getString(id)

    /**
     * Turns a drawable into a bitmap
     */
    fun resourceToBitmap(resourceId: Int): Bitmap {
        return BitmapFactory.decodeResource(
            ApplicationProvider.getApplicationContext<Context>().resources,
            resourceId
        )
    }

    /**
     * Matches a view with the drawable [id].
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

    /**
     * Returns the test current activity
     */
    fun <A : Activity> getActivity(scenario: ActivityScenario<A>): A {
        var activity: A? = null
        scenario.onActivity {
            activity = it
        }
        return activity!!
    }

    /**
     * Code to test gallery : https://proandroiddev.com/testing-camera-and-galley-intents-with-espresso-218eb9f59da9
     */
    fun savePickedImage(activity: Activity, resId: Int) {
        val bm = BitmapFactory.decodeResource(activity.resources, resId)
        val dir = activity.externalCacheDir
        val file = File(dir?.path, "pickImageResult.jpeg")
        val outStream: FileOutputStream?
        try {
            outStream = FileOutputStream(file)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            with(outStream) {
                flush()
                close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createImageGallerySetResultStub(activity: Activity): Instrumentation.ActivityResult {
        val resultData = Intent()
        val dir = activity.externalCacheDir
        val file = File(dir?.path, "pickImageResult.jpeg")
        val uri = Uri.fromFile(file)
        resultData.data = uri
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    fun isExpanded(): Matcher<in View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("SlidingUpPanel that is expanded")
        }

        override fun matchesSafely(item: View): Boolean {
            return item is SlidingUpPanelLayout &&
                    item.panelState == SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }

    fun isHidden(): Matcher<in View> = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("SlidingUpPanel that is hidden")
        }

        override fun matchesSafely(item: View): Boolean {
            return item is SlidingUpPanelLayout
                    && item.panelState == SlidingUpPanelLayout.PanelState.HIDDEN
        }
    }

    /**
     * Check that the Snackbar at the bottom of the current view displays [text]
     */
    fun checkSnackbarText(text: String) {
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(text)))
    }

}