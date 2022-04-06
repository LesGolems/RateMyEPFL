package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.util.AdapterUtil
import com.github.sdp.ratemyepfl.model.review.Review

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(AdapterUtil.diffCallback<Review>()) {

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
        private var currentReview: Review? = null


        fun bind(review: Review) {
            currentReview = review
            titleView.text = review.title
            rateView.text = review.rating.toString()
            commentView.text = review.comment
            dateView.text = review.date.toString()
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