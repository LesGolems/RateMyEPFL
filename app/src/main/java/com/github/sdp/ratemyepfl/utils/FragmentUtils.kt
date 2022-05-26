package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.ui.adapter.post.OnClickListener
import com.google.android.material.snackbar.Snackbar

object FragmentUtils {

    /**
     * Displays [message] in a [Toast] at the bottom of the current view
     */
    fun displayOnToast(context: Context, message: String?) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Displays [message] in a [Snackbar] at the bottom of the current view
     */
    fun displayOnSnackbar(view: View, message: String?) {
        message?.let {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Creates the [OnClickListener] from function [f] and displays an exception message if any
     */
    fun <T : Post> getListener(f: (T, String?) -> Unit, view: View) =
        OnClickListener<T> { postWithAuthor ->
            try {
                f(postWithAuthor.post, postWithAuthor.author?.uid)
            } catch (e: Exception) {
                displayOnSnackbar(view, e.message)
            }
        }

    /**
     * Displays the elements of [list] or the message [emptyText] if empty
     */
    fun emptyList(isEmpty: Boolean, list: RecyclerView, emptyText: TextView) {
        when (isEmpty) {
            true -> {
                emptyText.visibility = View.VISIBLE
                list.visibility = View.GONE
            }
            false -> {
                emptyText.visibility = View.GONE
                list.visibility = View.VISIBLE
            }
        }
    }
}