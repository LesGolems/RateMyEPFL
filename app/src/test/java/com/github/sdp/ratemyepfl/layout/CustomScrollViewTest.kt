package com.github.sdp.ratemyepfl.layout

import android.view.View
import org.junit.Assert.*
import org.junit.Test

class CustomScrollViewTest {

    companion object {
        private val CUSTOM_SCROLL_VIEW_1 = CustomScrollView(null)
        private val CUSTOM_SCROLL_VIEW_2 = CustomScrollView(null, null)
        private val CUSTOM_SCROLL_VIEW_3 = CustomScrollView(null, null, 0)
    }

    @Test
    fun constructorsWork() {
        assertNotNull(CUSTOM_SCROLL_VIEW_1)
        assertNotNull(CUSTOM_SCROLL_VIEW_2)
        assertNotNull(CUSTOM_SCROLL_VIEW_3)
    }

    @Test
    fun addInterceptScrollViewWorks() {
        val view = View(null)
        CUSTOM_SCROLL_VIEW_1.addInterceptScrollView(view)
        assertTrue(CUSTOM_SCROLL_VIEW_1.mInterceptScrollViews.contains(view))
    }

    @Test
    fun removeInterceptScrollViewWorks() {
        val view = View(null)
        CUSTOM_SCROLL_VIEW_1.addInterceptScrollView(view)
        CUSTOM_SCROLL_VIEW_1.removeInterceptScrollView(view)
        assertFalse(CUSTOM_SCROLL_VIEW_1.mInterceptScrollViews.contains(view))
    }
}