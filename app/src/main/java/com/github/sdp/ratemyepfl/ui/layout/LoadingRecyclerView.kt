package com.github.sdp.ratemyepfl.ui.layout

import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import kotlinx.coroutines.coroutineScope

class LoadingRecyclerView(val recyclerView: RecyclerView, progressBar: ProgressBar, val textView: TextView) : LoadingView<RecyclerView>(recyclerView, progressBar) {

    constructor(layout: View) : this(layout.findViewById<RecyclerView>(R.id.loadingRecyclerView),
    layout.findViewById<ProgressBar>(R.id.loadingRecyclerViewProgressBar),
    layout.findViewById<TextView>(R.id.loadindRecyclerViewProgressBarText))

    fun setOnReachBottom(action: () -> Unit) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    action()
                }
            }
        })
    }
}