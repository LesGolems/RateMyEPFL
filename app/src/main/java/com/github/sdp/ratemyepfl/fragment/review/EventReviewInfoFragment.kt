package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.review.EventInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventReviewInfoFragment : Fragment(R.layout.fragment_event_review_info) {
    // Gets the shared view model
    private val viewModel by activityViewModels<EventInfoViewModel>()

    private lateinit var eventIdInfo: TextView
    private lateinit var eventNumReview: TextView
    private lateinit var eventRatingBar: RatingBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventIdInfo =  view.findViewById(R.id.eventIdInfo)
        eventNumReview = view.findViewById(R.id.eventNumReview)
        eventRatingBar = view.findViewById(R.id.eventRatingBar)

        viewModel.event.observe(viewLifecycleOwner) {
            eventIdInfo.text = it?.toString()
            eventNumReview.text =
                getNumReviewString(requireContext(), it.numReviews)
            eventRatingBar.rating = it.grade.toFloat()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateEvent()
    }
}