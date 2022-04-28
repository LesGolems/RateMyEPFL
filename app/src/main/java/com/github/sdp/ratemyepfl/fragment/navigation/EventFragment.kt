package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.EventListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : ReviewableTabFragment() {
    private val viewModel: EventListViewModel by viewModels()

    override val reviewActivityLayoutId: Int = R.layout.activity_event_review

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.events
            .observe(viewLifecycleOwner) { events ->
                reviewableAdapter.submitData(events)
            }
    }

    override fun onResume() {
        viewModel.events.postValue(viewModel.events.value ?: listOf())
        super.onResume()
    }
}