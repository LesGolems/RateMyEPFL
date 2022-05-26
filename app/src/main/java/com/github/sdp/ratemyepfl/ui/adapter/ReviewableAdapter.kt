package com.github.sdp.ratemyepfl.ui.adapter

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.AdapterUtil

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
        private val layout: FrameLayout = reviewableView.findViewById(R.id.ribbon_main)

        private var currentReviewable: Reviewable? = null

        init {
            reviewableView.isClickable = true
            reviewableView.findViewById<CardView>(R.id.reviewableItemLayout)
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
            setRibbonColor(reviewable, layout)
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

    private fun setRibbonColor(reviewable: Reviewable, layout: FrameLayout) {
        when {
            reviewable.numReviews == 0 -> {
                layout.background.setColorFilter(
                    Color.parseColor("#ff807F7F"),
                    PorterDuff.Mode.SRC_ATOP
                );
            }
            reviewable.grade <= 2 -> {
                layout.background.setColorFilter(
                    Color.parseColor("#ffFA1313"),
                    PorterDuff.Mode.SRC_ATOP
                );
            }
            reviewable.grade <= 3 -> {
                layout.background.setColorFilter(
                    Color.parseColor("#ffFFB51E"),
                    PorterDuff.Mode.SRC_ATOP
                );
            }
            reviewable.grade <= 4 -> {
                layout.background.setColorFilter(
                    Color.parseColor("#ff99E44C"),
                    PorterDuff.Mode.SRC_ATOP
                );
            }
            reviewable.grade <= 5 -> {
                layout.background.setColorFilter(
                    Color.parseColor("#ff99CF04"),
                    PorterDuff.Mode.SRC_ATOP
                );
            }
        }
    }
}