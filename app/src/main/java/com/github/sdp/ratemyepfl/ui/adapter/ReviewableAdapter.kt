package com.github.sdp.ratemyepfl.ui.adapter

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.utils.AdapterUtil
import java.math.RoundingMode
import java.text.DecimalFormat

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
        private val layout: FrameLayout = reviewableView.findViewById(R.id.gradeLayout)
        private val grade: TextView = layout.findViewById(R.id.reviewableGrade)
        private val imageSpan: ImageSpan =
            ImageSpan(reviewableView.context, R.drawable.ic_baseline_star_24)

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
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            val gradeText = df.format(reviewable.grade) + " "
            val sb = SpannableStringBuilder(gradeText)
            sb.setSpan(
                imageSpan,
                gradeText.length - 1,
                gradeText.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            grade.text = sb
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