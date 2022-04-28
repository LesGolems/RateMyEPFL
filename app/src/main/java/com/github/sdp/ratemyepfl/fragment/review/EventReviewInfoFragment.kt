package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.EventInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventReviewInfoFragment : Fragment(R.layout.fragment_event_review_info) {
    // Gets the shared view model
    private val viewModel by activityViewModels<EventInfoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.event.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.roomIdInfo).text = it?.toString()
            view.findViewById<TextView>(R.id.roomNumReview).text = getNumReviewString(it.numReviews)
            view.findViewById<RatingBar>(R.id.roomRatingBar).rating = it.averageGrade.toFloat()
        }
    }

    private fun getNumReviewString(numReview: Int): String {
        return if (numReview == 0) {
            getString(R.string.zero_num_reviews)
        } else {
            getString(R.string.num_reviews, numReview.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateEvent()
    }
}