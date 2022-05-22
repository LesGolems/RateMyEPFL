package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
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
import com.github.sdp.ratemyepfl.utils.InfoFragmentUtils.getNumReviewString
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.utils.SoundDisplayUtils
import com.github.sdp.ratemyepfl.utils.TimeUtils
import com.github.sdp.ratemyepfl.viewmodel.ClassroomInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment displayed all relevant information for a Classroom
 */
@AndroidEntryPoint
class RoomReviewInfoFragment : Fragment(R.layout.fragment_room_review_info) {

    // Gets the shared view model
    private val viewModel by activityViewModels<ClassroomInfoViewModel>()

    private lateinit var roomNoiseInfoTextView: TextView
    private lateinit var noiseMeasureButton: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.room.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.roomIdInfo).text = it?.toString()
            view.findViewById<TextView>(R.id.roomNumReview).text =
                getNumReviewString(requireContext(), it.numReviews)
            view.findViewById<RatingBar>(R.id.roomRatingBar).rating = it.grade.toFloat()
        }

        roomNoiseInfoTextView = view.findViewById(R.id.roomNoiseInfoTextView)
        viewModel.noiseData.observe(viewLifecycleOwner) {
            displayRoomNoise(it)
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

    private fun displayRoomNoise(noiseData: List<NoiseInfo>) {
        if (noiseData.isEmpty()) return

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