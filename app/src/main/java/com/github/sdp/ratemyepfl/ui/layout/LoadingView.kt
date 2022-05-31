package com.github.sdp.ratemyepfl.ui.layout

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import com.github.sdp.ratemyepfl.backend.database.query.QueryResult
import com.github.sdp.ratemyepfl.backend.database.query.QueryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * A View that is animated while loading. It allows smooth display for I/O operations.
 *
 * It requires a [View] to manipulate and a [ProgressBar] that displays the animation.
 */
open class LoadingView<T: View>(val view: T, val progressBar: ProgressBar) {

    open suspend fun<U> display(result: Flow<U>, onCompletion: (U) -> Unit, onError: (String) -> Unit = { makeToastError(it) }) {
        startLoading()
        withTimeoutOrNull(LOADING_TIMEOUT_MS) {
            result
                .catch { it.message?.run(onError) }
                .collect {
                    onCompletion(it)
                }
            stopLoading()
        } ?: onError(TIMEOUT_ERROR_MESSAGE)
    }

    private fun startLoading() {
        progressBar.visibility = VISIBLE
        view.visibility = INVISIBLE
        progressBar.animate()
    }

    private fun stopLoading() {
        progressBar.visibility = INVISIBLE
        view.visibility = VISIBLE
    }

    private fun makeToastError(message: String) {
        Toast.makeText(view.context, message, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        const val LOADING_TIMEOUT_MS = 10000L
        const val TIMEOUT_ERROR_MESSAGE = "An error occurred. Please try again."
    }
}