package com.github.sdp.ratemyepfl.ui.fragment.review

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.NoiseInfo
import com.github.sdp.ratemyepfl.model.items.Classroom
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.utils.SoundDisplayUtils
import com.github.sdp.ratemyepfl.utils.TimeUtils
import com.github.sdp.ratemyepfl.viewmodel.review.ClassroomInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Classroom
 */
@AndroidEntryPoint
class RoomReviewInfoFragment : Fragment(R.layout.fragment_room_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<ClassroomInfoViewModel>()

    private lateinit var roomIdInfo: TextView
    private lateinit var roomNumReview: TextView
    private lateinit var roomRatingBar: RatingBar

    private lateinit var roomNoiseInfoTextView: TextView
    private lateinit var noiseMeasureButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomIdInfo = view.findViewById(R.id.roomCode)
        roomNumReview = view.findViewById(R.id.roomNumReview)
        roomRatingBar = view.findViewById(R.id.roomRatingBar)
        roomNoiseInfoTextView = view.findViewById(R.id.roomNoiseInfo)
        noiseMeasureButton = view.findViewById(R.id.noiseMeasureButton)

        viewModel.room.observe(viewLifecycleOwner) {
            setUpRoomInfo(it)
        }
        viewModel.noiseData.observe(viewLifecycleOwner) {
            displayRoomNoise(it)
        }

        val audioPermissionLauncher =
            PermissionUtils.requestPermissionLauncher({ startAudio() }, this, requireContext())
        noiseMeasureButton.setOnClickListener {
            PermissionUtils.verifyPermissionAndExecute(
                { startAudio() },
                audioPermissionLauncher,
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            )
        }
    }

    private fun setUpRoomInfo(room: Classroom) {
        roomIdInfo.text = room.toString()
        roomNumReview.text =
            getNumReviewString(requireContext(), room.numReviews)
        roomRatingBar.rating = room.grade.toFloat()
    }

    private fun displayRoomNoise(noiseData: List<NoiseInfo>) {
        if (noiseData.isEmpty()) {
            roomNoiseInfoTextView.text = getString(R.string.noise_no_measure)
            roomNoiseInfoTextView.setTextColor(Color.BLACK)
            return
        }

        val sortedList = noiseData.sortedBy {
            it.date
        }
        val mostRecentDate = sortedList.last().date
        val mostRecentMeasure = noiseData.first {
            it.date == mostRecentDate
        }.measure

        val (text, color, _) = SoundDisplayUtils.decibelMap(mostRecentMeasure)
        roomNoiseInfoTextView.text =
            getString(
                R.string.room_noise_info,
                text,
                mostRecentMeasure.toString(),
                TimeUtils.prettyTime(mostRecentDate)
            )
        roomNoiseInfoTextView.setTextColor(color)
    }


    /**
     * Gets the noise intensity that was measured by the user in [AudioRecordFragment]
     */
    private fun startAudio() {
        Navigation.findNavController(requireView()).navigate(R.id.audioRecordFragment)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}