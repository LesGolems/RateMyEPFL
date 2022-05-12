package com.github.sdp.ratemyepfl.fragment.review

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.viewmodel.ClassroomInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Classroom
 */
@AndroidEntryPoint
class RoomReviewInfoFragment : Fragment(R.layout.fragment_room_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<ClassroomInfoViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.room.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.roomIdInfo).text = it?.toString()
            view.findViewById<TextView>(R.id.roomNumReview).text = getNumReviewString(requireContext(), it.numReviews)
            view.findViewById<RatingBar>(R.id.roomRatingBar).rating = it.grade.toFloat()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateRoom()
    }
}