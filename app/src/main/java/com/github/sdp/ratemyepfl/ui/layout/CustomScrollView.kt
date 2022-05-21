package com.github.sdp.ratemyepfl.ui.layout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView

/**
 * A custom scroll view that allows child views to intercept scroll events
 */
class CustomScrollView : ScrollView {
    var mInterceptScrollViews: MutableList<View> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun addInterceptScrollView(view: View) {
        Log.i(null, "issou add")
        mInterceptScrollViews.add(view)
    }

    fun removeInterceptScrollView(view: View) {
        mInterceptScrollViews.remove(view)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (mInterceptScrollViews.size > 0) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val bounds = Rect()
            for (view in mInterceptScrollViews) {
                view.getHitRect(bounds)
                if (bounds.contains(x, y + scrollY)) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }
}