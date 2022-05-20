package com.github.sdp.ratemyepfl.adapter

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewWithAuthor
import com.github.sdp.ratemyepfl.viewmodel.UserViewModel

class ReviewAdapter(
    override val lifecycleOwner: LifecycleOwner,
    override val userViewModel: UserViewModel,
    override val likeListener: OnClickListener<Review>,
    override val dislikeListener: OnClickListener<Review>,
    override val deleteListener: OnClickListener<Review>,
    override val profileClickListener: OnClickListener<Review>,
) : PostAdapter<Review>(
    lifecycleOwner,
    userViewModel,
    likeListener,
    dislikeListener,
    deleteListener,
    profileClickListener,
    R.layout.review_item
) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ReviewViewHolder(reviewView: View) : PostViewHolder(reviewView) {
        private val rateView: TextView = reviewView.findViewById(R.id.rateReview)

        override fun bind(postWithAuthor: ReviewWithAuthor) {
            rateView.text = post.rating.toString()
        }
    }

}