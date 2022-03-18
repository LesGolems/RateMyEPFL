package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.Review

class RoomReviewsAdapter :
    ListAdapter<Review, RoomReviewsAdapter.RoomReviewViewHolder>(RoomReviewDiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class RoomReviewViewHolder(reviewView: View) :
        RecyclerView.ViewHolder(reviewView) {

        private val gradeTextView: TextView =
            reviewView.findViewById(R.id.room_review_grade)
        private val commentTextView: TextView =
            reviewView.findViewById(R.id.room_review_comment)
        private val dateTextView : TextView =
            reviewView.findViewById(R.id.room_review_date)
        private var currentReview: Review? = null


        /* Bind room id and name. */
        fun bind(review: Review) {
            currentReview = review
            ("Grade : " + review.rating.toString()).also { gradeTextView.text = it }
            ("Comment : " + review.comment).also { commentTextView.text = it }
            ("Posted on : " + review.date.toString()).also { dateTextView.text = it } // not very clean way to do this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomReviewViewHolder {
        return RoomReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_review_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoomReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

}

object RoomReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.comment == newItem.comment
    }
}