package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekn.gruzer.gaugelibrary.ArcGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.EventInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventReviewInfoFragment : Fragment(R.layout.fragment_event_review_info) {

    private val viewModel by activityViewModels<EventInfoViewModel>()

    private lateinit var eventName: TextView
    private lateinit var eventNumReview: TextView
    private lateinit var eventRatingBar: RatingBar
    private lateinit var eventCreator: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventTime: TextView
    private lateinit var participationGauge: ArcGauge

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventName = view.findViewById(R.id.eventName)
        eventNumReview = view.findViewById(R.id.eventNumReview)
        eventRatingBar = view.findViewById(R.id.eventRatingBar)
        eventCreator = view.findViewById(R.id.eventCreator)
        eventDate = view.findViewById(R.id.eventDate)
        eventTime = view.findViewById(R.id.eventTime)
        participationGauge = view.findViewById(R.id.participationGauge)

        viewModel.event.observe(viewLifecycleOwner) {
            eventName.text = it.toString()
            eventCreator.text = getString(R.string.event_creator_info, it.creator)
            eventNumReview.text = getNumReviewString(requireContext(), it.numReviews)
            eventRatingBar.rating = it.grade.toFloat()
            eventDate.text = it.date.toLocalDate().toString()
            eventTime.text = it.date.toLocalTime().toString()
            setParticipationGauge(it)
        }
    }

    /**
     * Set the participation gauge, i.e. its value, its color and its bounds
     */
    private fun setParticipationGauge(event: Event) {
        participationGauge.minValue = 0.0
        val value = event.numParticipants
        val maxValue = event.limitParticipants
        participationGauge.maxValue = maxValue.toDouble()
        participationGauge.value = value.toDouble()

        val range = Range()
        range.color = resources.getColor(R.color.secondaryColor)
        range.from = 0.0
        range.to = maxValue.toDouble()
        participationGauge.addRange(range)

        participationGauge.setFormatter {
            "$value/$maxValue"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateEvent()
    }
}