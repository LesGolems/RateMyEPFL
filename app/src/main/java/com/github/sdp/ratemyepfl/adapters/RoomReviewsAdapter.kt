package com.github.sdp.ratemyepfl.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.review.ClassroomReview
import com.github.sdp.ratemyepfl.R

class RoomReviewsAdapter :
    ListAdapter<ClassroomReview, RoomReviewsAdapter.RoomReviewViewHolder>(RoomReviewDiffCallback) {

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
        private var currentReview: ClassroomReview? = null


        /* Bind room id and name. */
        fun bind(review: ClassroomReview) {
            currentReview = review
            gradeTextView.text = review.rate.toString()
            commentTextView.text = review.comment
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

object RoomReviewDiffCallback : DiffUtil.ItemCallback<ClassroomReview>() {
    override fun areItemsTheSame(oldItem: ClassroomReview, newItem: ClassroomReview): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ClassroomReview, newItem: ClassroomReview): Boolean {
        return oldItem.comment == newItem.comment
    }
}