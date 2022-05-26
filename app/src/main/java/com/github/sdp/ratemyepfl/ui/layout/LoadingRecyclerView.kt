package com.github.sdp.ratemyepfl.ui.layout

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

class LoadingRecyclerView(
    val recyclerView: RecyclerView,
    progressBar: ProgressBar,
    val textView: TextView
) : LoadingView<RecyclerView>(recyclerView, progressBar) {

    constructor(layout: View) : this(
        layout.findViewById<RecyclerView>(R.id.loadingRecyclerView),
        layout.findViewById<ProgressBar>(R.id.loadingRecyclerViewProgressBar),
        layout.findViewById<TextView>(R.id.loadindRecyclerViewProgressBarText)
    )

    suspend fun<T> display(result: Flow<List<T>>, onSuccess: (List<T>) -> Unit) {
        startLoading()
        result.catch {
            it.message?.run {
                displayTextMessage("Failed to load posts")
            }

        }
            .collect {
                if (it.isEmpty()) {
                    displayTextMessage("No post for now...")
                } else {
                    onSuccess(it)
                    displayList()
                }

            }
    }

    private fun startLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        textView.visibility = View.INVISIBLE
        progressBar.animate()
    }


    private fun stopLoading() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun displayList() {
        recyclerView.visibility = View.VISIBLE
        textView.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    private fun displayTextMessage(message: String) {
        textView.text = message
        textView.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
    }

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