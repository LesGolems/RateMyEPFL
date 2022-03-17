package com.github.sdp.ratemyepfl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.review.CourseReview

class CourseReviewAdapter(
    context: Context,
    resource: Int,
    reviews: List<CourseReview>
) : ArrayAdapter<CourseReview>(context, resource, reviews) {

    /** TO DO:
     *  - Refactor in ListAdapter
     *  - Link the database and the content of the viewModel. The adapter
     *      is bridge between the ListView and the activity, but still
     *      holds a fixed value of the reviews. This can be done with
     *      observers, as done in RoomReviewsList
     */

    private val mResource: Int = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)

        val convertedView = layoutInflater.inflate(mResource, parent, false)

        val titleView: TextView = convertedView.findViewById(R.id.titleReview)
        titleView.text = getItem(position)?.title ?: ""

        val commentView: TextView = convertedView.findViewById(R.id.commentReview)
        commentView.text = getItem(position)?.comment ?: ""

        val dateView: TextView = convertedView.findViewById(R.id.dateReview)
        dateView.text = getItem(position)?.date.toString()

        val rateView: TextView = convertedView.findViewById(R.id.rateReview)
        rateView.text = getItem(position)?.rating.toString()

        return convertedView
    }
}