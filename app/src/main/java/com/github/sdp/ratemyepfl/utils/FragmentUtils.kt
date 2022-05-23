package com.github.sdp.ratemyepfl.utils

import android.content.Context
import android.widget.Toast
import com.github.sdp.ratemyepfl.model.review.Post
import com.github.sdp.ratemyepfl.ui.adapter.post.OnClickListener

object FragmentUtils {

    fun displayOnToast(context: Context, message: String?) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Creates the onClickListener from the function given as input, encapsulating it in a
     * try catch to display the error message as SnackBar
     */
    fun <T : Post> getListener(f: (T, String?) -> Unit, context: Context) =
        OnClickListener<T> { postWithAuthor ->
            try {
                f(postWithAuthor.post, postWithAuthor.author?.uid)
            } catch (e: Exception) {
                e.message?.let {
                    displayOnToast(context, it)
                }
            }
        }
}