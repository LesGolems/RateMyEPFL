package com.github.sdp.ratemyepfl.utils

import android.content.Context
import com.github.sdp.ratemyepfl.R

object InfoFragmentUtils {

    fun getNumReviewString(context: Context, numReview: Int): String {
        return if (numReview == 0) {
            context.getString(R.string.zero_num_reviews)
        } else {
            context.getString(R.string.num_reviews, numReview.toString())
        }
    }
}