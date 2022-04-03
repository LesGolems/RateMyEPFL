package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewOpinion
import com.github.sdp.ratemyepfl.utils.ListActivityUtils

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ListActivityUtils.diffCallback<Review>()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ReviewViewHolder(reviewView: View) :
        RecyclerView.ViewHolder(reviewView) {

        private val rateView: TextView =
            reviewView.findViewById(R.id.rateReview)
        private val commentView: TextView =
            reviewView.findViewById(R.id.commentReview)
        private val dateView: TextView =
            reviewView.findViewById(R.id.dateReview)
        private val titleView: TextView =
            reviewView.findViewById(R.id.titleReview)

        private var likeButton: ImageButton =
            reviewView.findViewById(R.id.likeButton)
        private var dislikeButton: ImageButton =
            reviewView.findViewById(R.id.dislikeButton)

        private var likeCountTextView: TextView =
            reviewView.findViewById(R.id.likeCount)
        private var dislikeCountTextView: TextView =
            reviewView.findViewById(R.id.dislikeCount)

        private var currentReview: Review? = null


        fun bind(review: Review) {
            currentReview = review
            titleView.text = review.title
            rateView.text = review.rating.toString()
            commentView.text = review.comment
            dateView.text = review.date.toString()

            likeCountTextView.text = "0"
            dislikeCountTextView.text = "0"

            likeButton.setOnClickListener {
                if (review.opinion == ReviewOpinion.LIKED) {
                    review.opinion = ReviewOpinion.NO_OPINION
                    likeButton.setImageResource(R.drawable.ic_like)
                } else {
                    if (review.opinion == ReviewOpinion.DISLIKED) {
                        dislikeButton.setImageResource(R.drawable.ic_dislike)
                    }
                    review.opinion = ReviewOpinion.LIKED
                    likeButton.setImageResource(R.drawable.ic_like_toggled)
                }
            }

            dislikeButton.setOnClickListener {
                if (review.opinion == ReviewOpinion.DISLIKED) {
                    review.opinion = ReviewOpinion.NO_OPINION
                    dislikeButton.setImageResource(R.drawable.ic_dislike)
                } else {
                    if (review.opinion == ReviewOpinion.LIKED) {
                        likeButton.setImageResource(R.drawable.ic_like)
                    }
                    review.opinion = ReviewOpinion.DISLIKED
                    dislikeButton.setImageResource(R.drawable.ic_dislike_toggled)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.review_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

}