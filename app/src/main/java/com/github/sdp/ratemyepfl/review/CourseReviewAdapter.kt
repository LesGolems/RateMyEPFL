package com.github.sdp.ratemyepfl.review

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.github.sdp.ratemyepfl.R

class CourseReviewAdapter(
    context: Context,
    resource: Int,
    reviews: List<CourseReview>
) : ArrayAdapter<CourseReview>(context, resource, reviews) {

    private val mContext: Context = context
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