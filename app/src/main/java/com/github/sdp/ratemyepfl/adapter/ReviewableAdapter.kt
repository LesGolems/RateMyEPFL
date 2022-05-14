package com.github.sdp.ratemyepfl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.AdapterUtil
import com.github.sdp.ratemyepfl.model.items.Reviewable

class ReviewableAdapter(private val onClick: (Reviewable) -> Unit) :
    ListAdapter<Reviewable, ReviewableAdapter.ReviewableViewHolder>(AdapterUtil.diffCallback<Reviewable>()) {

    private var list: List<Reviewable> = listOf()

    fun submitData(data: List<Reviewable>, commitCallback: (() -> Unit) = {}) {
        list = data.toList()
        submitList(list, commitCallback)
    }

    inner class ReviewableViewHolder(reviewableView: View) :
        RecyclerView.ViewHolder(reviewableView) {

        private val reviewableTextView: TextView = reviewableView.findViewById(R.id.reviewableId)
        private var currentReviewable: Reviewable? = null

        init {
            reviewableView.isClickable = true
            reviewableView.findViewById<LinearLayout>(R.id.reviewableItemLayout)
                .setOnClickListener {
                    currentReviewable?.let {
                        onClick(it)
                    }
                }
        }

        /* Bind room id. */
        fun bind(reviewable: Reviewable) {
            currentReviewable = reviewable
            reviewableTextView.text = reviewable.toString()
        }
    }

    /* Creates and inflates view and return RoomViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewableViewHolder {
        return ReviewableViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reviewable_item,
                parent,
                false
            )
        )
    }

    /* Gets current room and uses it to bind view. */
    override fun onBindViewHolder(holder: ReviewableViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

}