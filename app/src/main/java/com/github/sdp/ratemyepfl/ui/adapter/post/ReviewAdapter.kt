package com.github.sdp.ratemyepfl.ui.adapter.post

import android.widget.RatingBar
import androidx.lifecycle.LifecycleOwner
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.viewmodel.profile.UserViewModel

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

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val postWithAuthor = getItem(position)

        val rateView: RatingBar = holder.itemView.findViewById(R.id.reviewRatingBar)
        rateView.rating = postWithAuthor.post.rating.rating.toFloat()
    }

}