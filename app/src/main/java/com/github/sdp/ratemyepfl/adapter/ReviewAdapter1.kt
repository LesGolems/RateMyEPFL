package com.github.sdp.ratemyepfl.adapter

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

class ReviewAdapter1(
    private val onLikeClick: (Review, Int) -> Unit,
    private val onDislikeClick: (Review, Int) -> Unit
) :
    ListAdapter<Review, ReviewAdapter1.ReviewViewHolder>(AdapterUtil.diffCallback<Review>()) {

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

        private var likesTextView: TextView =
            reviewView.findViewById(R.id.likeCount)
        private var dislikesTextView: TextView =
            reviewView.findViewById(R.id.dislikeCount)

        private var currentReview: Review? = null
        /*private var isLiked: Boolean = false
        private var isDisliked: Boolean = false*/

        private fun isLiked(view: View): Boolean {
            return view.tag == R.drawable.ic_like_toggled
        }

        private fun isDisliked(view: View): Boolean {
            return view.tag == R.drawable.ic_dislike_toggled
        }

        private fun setImage(button: ImageButton, resId: Int) {
            button.setImageResource(resId)
            button.tag = resId
        }


        fun bind(review: Review) {
            currentReview = review
            titleView.text = review.title
            rateView.text = review.rating.toString()
            commentView.text = review.comment
            dateView.text = review.date.toString()
            likesTextView.text = review.likes.toString()
            dislikesTextView.text = review.dislikes.toString()

            likeButton.setOnClickListener {
                if (isLiked(it)) {
                    //isLiked = false
                    currentReview?.let { r ->
                        onLikeClick(r, -1)
                    }
                    setImage(likeButton, R.drawable.ic_like)
                } else {
                    //isLiked = true
                    currentReview?.let { r ->
                        onLikeClick(r, 1)
                    }
                    setImage(likeButton, R.drawable.ic_like_toggled)
                    if (isDisliked(it)) {
                        //isDisliked = false
                        currentReview?.let { r ->
                            onDislikeClick(r, -1)
                        }
                        setImage(dislikeButton, R.drawable.ic_dislike)
                    }
                }
            }

            dislikeButton.setOnClickListener {
                if (isDisliked(it)) {
                    //isDisliked = false
                    currentReview?.let { r ->
                        onDislikeClick(r, -1)
                    }
                    setImage(dislikeButton, R.drawable.ic_dislike)
                } else {
                    //isDisliked = true
                    currentReview?.let { r ->
                        onDislikeClick(r, 1)
                    }
                    setImage(dislikeButton, R.drawable.ic_dislike_toggled)
                    if (isLiked(it)) {
                        //isLiked = false
                        currentReview?.let { r ->
                            onLikeClick(r, -1)
                        }
                        setImage(likeButton, R.drawable.ic_like)
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