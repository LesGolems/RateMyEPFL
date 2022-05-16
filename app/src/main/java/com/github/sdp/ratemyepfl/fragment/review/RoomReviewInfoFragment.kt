package com.github.sdp.ratemyepfl.fragment.review

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AudioRecordActivity
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

    private fun displayRoomNoise(noiseData: Map<String, Int>) {
        if (noiseData.isEmpty()) return

        val sortedMap = noiseData.toSortedMap()
        val mostRecentDate = sortedMap.lastKey()
        val mostRecentMeasure = sortedMap[mostRecentDate]

        val (text, color) = SoundDisplayUtils.decibelMap(mostRecentMeasure!!)
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
     * Gets the noise intensity that was measured by the user in [AudioRecordActivity]
     */
    private fun startAudio() {
        val intent = Intent(requireContext(), AudioRecordActivity::class.java)
        getAudioResults.launch(intent)
    }

    private val getAudioResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val measure = data?.getIntExtra(AudioRecordActivity.EXTRA_MEASUREMENT_VALUE, 0)
                if (measure != null) {
                    viewModel.submitNoiseMeasure(measure)
                    viewModel.refresh()
                    Toast.makeText(
                        requireContext(),
                        "Your measure ($measure dB) was uploaded successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}