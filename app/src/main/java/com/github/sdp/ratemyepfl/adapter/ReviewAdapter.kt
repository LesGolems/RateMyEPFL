package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.ImageFile
import com.github.sdp.ratemyepfl.model.review.Review
import com.github.sdp.ratemyepfl.model.review.ReviewWithAuthor
import com.github.sdp.ratemyepfl.model.user.User
import de.hdodenhof.circleimageview.CircleImageView

class ReviewAdapter(
    val likeListener: OnVoteClickListener,
    val dislikeListener: OnVoteClickListener
) :
    ListAdapter<ReviewWithAuthor, ReviewAdapter.ReviewViewHolder>(AdapterUtil.diffCallback<ReviewWithAuthor>()) {

    fun interface OnVoteClickListener {
        fun onClick(review: Review)
    }

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

        private val authorUsername: TextView = reviewView.findViewById(R.id.author_username)
        private val authorProfilePicture: CircleImageView = reviewView.findViewById(R.id.author_profile_picture)

        private lateinit var review: Review

        fun bind(reviewWithAuthor: ReviewWithAuthor) {
            review = reviewWithAuthor.review
            val author = reviewWithAuthor.author
            val image = reviewWithAuthor.image

            titleView.text = review.title
            rateView.text = review.rating.toString()
            commentView.text = review.comment
            dateView.text = review.date.toString()

            likesTextView.text = review.likers.size.toString()
            dislikesTextView.text = review.dislikers.size.toString()

            /* Dislike button logic */
            dislikeButton.setOnClickListener {
                dislikeListener.onClick(review)
            }

            /* Like button logic */
            likeButton.setOnClickListener {
                likeListener.onClick(review)
            }

            author?.let {
                authorUsername.setText(it.username)
            }

            image?.let {
                authorProfilePicture.setImageBitmap(it.data)
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