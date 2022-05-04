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
import com.github.sdp.ratemyepfl.auth.ConnectedUser
import com.github.sdp.ratemyepfl.model.items.Event
import com.github.sdp.ratemyepfl.model.items.Reviewable
import com.github.sdp.ratemyepfl.viewmodel.EventListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.layout_event_list) {
    private val viewModel: EventListViewModel by viewModels()
    private val eventAdapter =
        EventAdapter(
            { e -> displayReviews(e) },
            { e -> registrationListener(e) }
        )
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var auth: ConnectedUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        eventAdapter.setUserId(auth.getUserId())
        recyclerView.adapter = eventAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(activity?.applicationContext, DividerItemDecoration.VERTICAL)
        )

        viewModel.events
            .observe(viewLifecycleOwner) { events ->
                eventAdapter.submitList(events)
            }
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
            event.getId()
        )
        intent.putExtra(ReviewActivity.EXTRA_MENU_ID, R.menu.bottom_navigation_menu_event_review)
        intent.putExtra(ReviewActivity.EXTRA_GRAPH_ID, R.navigation.nav_graph_event_review)
        startActivity(intent)
    }

    /**
     * Update the registration of the user
     */
    private fun registrationListener(event: Event) {
        if (auth.isLoggedIn()) {
            val uid = auth.getUserId()!! // Can't be null as we checked that the user is logged in
            if (!viewModel.updateRegistration(event, uid)) {
                Snackbar.make(requireView(), R.string.full_event_text, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.activityMainBottomNavigationView)
                    .show()
            }
        } else {
            Snackbar.make(requireView(), R.string.registration_no_login_text, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.activityMainBottomNavigationView)
                .show()
        }
    }
}