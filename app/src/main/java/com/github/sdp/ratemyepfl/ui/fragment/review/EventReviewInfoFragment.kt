package com.github.sdp.ratemyepfl.ui.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ekn.gruzer.gaugelibrary.ArcGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.post.EventWithAuthor
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.review.EventInfoViewModel
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

        viewModel.eventWithAuthor.observe(viewLifecycleOwner) {
            setUpEventInfo(it)
        }
    }

    private fun setUpEventInfo(ewa: EventWithAuthor) {
        val event = ewa.obj
        eventName.text = event.toString()
        eventCreator.text = getString(R.string.event_creator_info, ewa.author?.username)
        eventNumReview.text = getNumReviewString(requireContext(), event.numReviews)
        eventRatingBar.rating = event.grade.toFloat()
        eventDate.text = event.period.start.date.toString()
        eventTime.text = event.period.start.time.toString()
        setParticipationGauge(event)
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
        range.color = ContextCompat.getColor(requireContext(), R.color.secondaryColor)
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