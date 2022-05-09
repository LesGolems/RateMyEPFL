package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AudioRecordTest
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.viewmodel.ClassroomInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Classroom
 */
@AndroidEntryPoint
class RoomReviewInfoFragment : Fragment(R.layout.fragment_room_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<ClassroomInfoViewModel>()

    private lateinit var noiseMeasureButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.room.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.roomIdInfo).text = it?.toString()
        }
        viewModel.numReviews.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.roomNumReview).text =
                getNumReviewString(requireContext(), it)
        }
        viewModel.averageGrade.observe(viewLifecycleOwner) {
            view.findViewById<RatingBar>(R.id.roomRatingBar).rating = it.toFloat()
        }

        // Record audio
        val audioPermissionLauncher =
            PermissionUtils.requestPermissionLauncher({ startAudio() }, this, requireContext())

        noiseMeasureButton = view.findViewById(R.id.noiseMeasureButton)
        noiseMeasureButton.setOnClickListener {
            PermissionUtils.startPhoneFeature(
                { startAudio() },
                audioPermissionLauncher,
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            )
        }

    }

    fun startAudio() {
        val intent = Intent(requireContext(), AudioRecordTest::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}