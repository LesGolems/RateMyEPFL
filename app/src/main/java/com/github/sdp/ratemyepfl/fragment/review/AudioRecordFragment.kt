package com.github.sdp.ratemyepfl.fragment.review

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaRecorder
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.adapter.RecordAdapter
import com.github.sdp.ratemyepfl.utils.SoundDisplayUtils
import com.github.sdp.ratemyepfl.viewmodel.ClassroomInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread
import kotlin.math.log10

@AndroidEntryPoint
class AudioRecordFragment : Fragment(R.layout.fragment_audio_record) {

    private lateinit var filename: String

    private lateinit var recordButton: ImageButton
    private lateinit var popUpButton: ImageButton
    private var recorder: MediaRecorder? = null
    private lateinit var loopingThread: Thread

    private lateinit var decibelTextView: TextView
    private lateinit var noiseTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var dialog: Dialog

    @Volatile
    private var averageIntensity: Double = 0.0

    @Volatile
    private var numberOfMeasures: Int = 0

    @Volatile
    private var start = false

    private val viewModel by activityViewModels<ClassroomInfoViewModel>()
    private lateinit var recordAdapter : RecordAdapter

    companion object {
        private const val referenceAmplitude = 25
        const val EXTRA_MEASUREMENT_VALUE: String = "com.github.sdp.extra_measurement_value"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Record to the external cache directory for visibility
        filename = "${requireActivity().externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.record_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popUpButton = view.findViewById(R.id.popUpButton)
        popUpButton.setOnClickListener {
            dialog.show()
        }

        recordButton = dialog.findViewById(R.id.audioRecordButton)
        recordButton.setOnClickListener {
            start = !start
            onRecord()
        }
        recyclerView = view.findViewById(R.id.recordRecyclerView)
        recordAdapter = RecordAdapter()
        recyclerView.adapter = recordAdapter
        viewModel.noiseData.observe(viewLifecycleOwner) {
            it?.let { recordAdapter.submitList(it) }
        }

        decibelTextView = dialog.findViewById(R.id.decibelTextView)
        noiseTextView = dialog.findViewById(R.id.noiseTextView)
    }

    /**
     * e.g. when the user quits the Activity while recording
     */
    override fun onPause() {
        super.onPause()
        if (start) {
            start = !start
            stopRecording()
        }
    }

    /**
     * e.g. when the user uploads its measure
     */
    override fun onStop() {
        super.onStop()
        if (start) {
            start = !start
            stopRecording()
        }
    }

    private fun onRecord() {
        if (start) {
            recordButton.setImageResource(R.drawable.stop_circle_outline)
            decibelTextView.visibility = View.VISIBLE
            noiseTextView.visibility = View.VISIBLE
            startRecording()
        } else {
            recordButton.setImageResource(R.drawable.record_circle)
            stopRecording()

            // Calculates the average sound intensity that was measured while recording
            // We do not take into account the first measure, which always gives a zero intensity
            if (numberOfMeasures > 0) {
                averageIntensity /= (numberOfMeasures - 1)
            }

            val measure = averageIntensity
            averageIntensity = 0.0
            numberOfMeasures = 0
            sendAudioResults(measure.toInt())
        }
    }

    private fun measureDecibels(): Double {
        val maxAmplitude = recorder!!.maxAmplitude.toDouble()
        return if (maxAmplitude == 0.0) {
            0.0
        } else {
            20 * log10(maxAmplitude / referenceAmplitude)
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filename)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            prepare()
            start()
        }

        // Measures the sound intensity every half-second until the user stops recording
        loopingThread = thread {
            while (start) {
                val soundIntensity = measureDecibels()
                requireActivity().runOnUiThread {
                    displayDecibels(soundIntensity.toInt())
                }
                averageIntensity += soundIntensity
                ++numberOfMeasures
                Thread.sleep(500)
            }
        }
    }

    private fun stopRecording() {
        loopingThread.join()
        recorder?.apply {
            reset()
            release()
        }
        recorder = null
    }

    private fun displayDecibels(intensity: Int) {
        val (text, color, _) = SoundDisplayUtils.decibelMap(intensity)
        decibelTextView.text = getString(R.string.decibels, intensity.toString())
        decibelTextView.setTextColor(color)
        noiseTextView.text = getString(R.string.noise, text)
        noiseTextView.setTextColor(color)
    }

    private fun sendAudioResults(measure: Int) {
        viewModel.submitNoiseMeasure(measure)
        dialog.hide()
    }

}
