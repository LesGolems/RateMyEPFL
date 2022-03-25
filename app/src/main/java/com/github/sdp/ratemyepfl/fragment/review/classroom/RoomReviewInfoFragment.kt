package com.github.sdp.ratemyepfl.fragment.review.classroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.viewmodel.RoomReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomReviewInfoFragment : Fragment(R.layout.fragment_room_review_info) {

    private val viewModel by activityViewModels<RoomReviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRoom().observe(viewLifecycleOwner){
            view.findViewById<TextView>(R.id.testinfo).text = it!!.id
        }
    }
}