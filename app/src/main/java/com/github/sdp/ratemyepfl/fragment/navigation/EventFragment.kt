package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.adapter.EventAdapter
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.EventListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.layout_event_list) {
    private val viewModel: EventListViewModel by viewModels()
    private val eventAdapter =
        EventAdapter(
            { e -> displayReviews(e) },
            { e -> registerListener(e) },
            { e -> viewModel.unregister(e) }
        )
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        recyclerView.adapter = eventAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.events
            .observe(viewLifecycleOwner) { events ->
                eventAdapter.submitList(events)
            }
        viewModel.events.value
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateEventsList()
    }

    /**
     * Starts a [EventActivity] for the corresponding [Reviewable].
     * @param reviewable: the reviewable for which we display the reviews
     */
    private fun displayReviews(event: Event) {
        val intent = Intent(activity?.applicationContext, ReviewActivity::class.java)
        intent.putExtra(
            ReviewActivity.EXTRA_ITEM_REVIEWED,
            event.id
        )
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_event_review)
        startActivity(intent)
    }

    private fun registerListener(event: Event) {
        if (event.numParticipants == event.limitParticipants) {
            Snackbar.make(requireView(), R.string.full_event_text, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.activityMainBottomNavigationView)
                .show()
        } else {
            viewModel.register(event)
        }
    }
}