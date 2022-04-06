package com.github.sdp.ratemyepfl.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewOpinion

class ReviewAdapter(
    private val onLikeClick: (Review, Int) -> Unit,
    private val onDislikeClick: (Review, Int) -> Unit,
    private val onOpinionRequest: (Review) -> ReviewOpinion?,
    private val setOpinion: (Review, ReviewOpinion) -> Unit,
) :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(AdapterUtil.diffCallback<Review>()) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ReviewViewHolder(reviewView: View) :
        RecyclerView.ViewHolder(reviewView) {

        private val titleView: TextView = reviewView.findViewById(R.id.titleReview)
        private val rateView: TextView = reviewView.findViewById(R.id.rateReview)
        private val commentView: TextView = reviewView.findViewById(R.id.commentReview)
        private val dateView: TextView = reviewView.findViewById(R.id.dateReview)
        private val likesTextView: TextView = reviewView.findViewById(R.id.likeCount)
        private val dislikesTextView: TextView = reviewView.findViewById(R.id.dislikeCount)
        private val likeButton: ImageButton = reviewView.findViewById(R.id.likeButton)
        private val dislikeButton: ImageButton = reviewView.findViewById(R.id.dislikeButton)

        private var currentReview: Review? = null


        fun bind(review: Review) {
            currentReview = review
            titleView.text = review.title
            rateView.text = review.rating.toString()
            commentView.text = review.comment
            dateView.text = review.date.toString()
            likesTextView.text = review.likes.toString()
            dislikesTextView.text = review.dislikes.toString()

            /* Like button logic */
            likeButton.setOnClickListener {
                val opinion: ReviewOpinion? = currentReview?.let { r -> onOpinionRequest(r) }
                Log.d("OPINION (L): ", opinion.toString())

                /* Remove the like from a liked review */
                if (opinion == ReviewOpinion.LIKED) {
                    likeButton.setImageResource(R.drawable.ic_like)
                    currentReview?.let { r ->
                        onLikeClick(r, -1)
                        setOpinion(r, ReviewOpinion.NO_OPINION)
                    }
                } else {
                    /* Like a review that was not liked before */
                    likeButton.setImageResource(R.drawable.ic_like_toggled)
                    currentReview?.let { r ->
                        onLikeClick(r, 1)
                        setOpinion(r, ReviewOpinion.LIKED)
                    }
                    /* Like a disliked review */
                    if (opinion == ReviewOpinion.DISLIKED) {
                        dislikeButton.setImageResource(R.drawable.ic_dislike)
                        currentReview?.let { r ->
                            onDislikeClick(r, -1)
                            setOpinion(r, ReviewOpinion.LIKED)
                        }
                    }
                }
            }

            /* Dislike button logic */
            dislikeButton.setOnClickListener {
                val opinion = currentReview?.let { r -> onOpinionRequest(r) }
                Log.d("OPINION (D): ", opinion.toString())

                /* Remove the dislike from a disliked review */
                if (opinion == ReviewOpinion.DISLIKED) {
                    dislikeButton.setImageResource(R.drawable.ic_dislike)
                    currentReview?.let { r ->
                        onDislikeClick(r, -1)
                        setOpinion(r, ReviewOpinion.NO_OPINION)
                    }
                } else {
                    /* Dislike a review that was not disliked before */
                    dislikeButton.setImageResource(R.drawable.ic_dislike_toggled)
                    currentReview?.let { r ->
                        onDislikeClick(r, 1)
                        setOpinion(r, ReviewOpinion.DISLIKED)
                    }
                    /* Dislike a liked review */
                    if (opinion == ReviewOpinion.LIKED) {
                        likeButton.setImageResource(R.drawable.ic_like)
                        currentReview?.let { r ->
                            onLikeClick(r, -1)
                            setOpinion(r, ReviewOpinion.DISLIKED)
                        }
                    }
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