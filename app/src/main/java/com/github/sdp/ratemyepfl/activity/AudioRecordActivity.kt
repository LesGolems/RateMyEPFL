package com.github.sdp.ratemyepfl.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread
import kotlin.math.log10

@AndroidEntryPoint
open class AudioRecordActivity : AppCompatActivity() {

    private lateinit var filename: String

    private lateinit var recordButton: ImageButton
    private var recorder: MediaRecorder? = null
    private lateinit var loopingThread: Thread

    private lateinit var decibelTextView: TextView
    private lateinit var noiseTextView: TextView

    @Volatile
    private var averageIntensity: Double = 0.0

    @Volatile
    private var numberOfMeasures: Int = 0

    @Volatile
    private var start = false

    companion object {
        private const val referenceAmplitude = 10
        const val EXTRA_MEASUREMENT_VALUE: String = "com.github.sdp.extra_measurement_value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        // Record to the external cache directory for visibility
        filename = "${externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        recordButton = findViewById(R.id.audioRecordButton)
        recordButton.setOnClickListener {
            start = !start
            onRecord()
        }

        decibelTextView = findViewById(R.id.decibelTextView)
        noiseTextView = findViewById(R.id.noiseTextView)
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
        Log.d("STOP", "STOP")
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
            sendAudioResults(measure)
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
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }.apply {
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
                runOnUiThread {
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
        decibelTextView.text = getString(R.string.decibels, intensity.toString())

        if (intensity < 30) { // Faint
            decibelTextView.setTextColor(Color.CYAN)
            noiseTextView.text = getString(R.string.noise, "Quiet")
            noiseTextView.setTextColor(Color.CYAN)
        } else if (intensity < 60) { // Moderate to quiet
            decibelTextView.setTextColor(Color.GREEN)
            noiseTextView.text = getString(R.string.noise, "Calm")
            noiseTextView.setTextColor(Color.GREEN)
        } else if (intensity < 90) { // Loud
            decibelTextView.setTextColor(Color.YELLOW)
            noiseTextView.text = getString(R.string.noise, "Loud")
            noiseTextView.setTextColor(Color.YELLOW)
        } else { // Extremely loud
            decibelTextView.setTextColor(Color.RED)
            noiseTextView.text = getString(R.string.noise, "Extremely loud")
            noiseTextView.setTextColor(Color.RED)
        }
    }

    private fun sendAudioResults(measure: Double) {
        val intent = Intent()
        intent.putExtra(EXTRA_MEASUREMENT_VALUE, measure)
        setResult(Activity.RESULT_OK, intent)
        Toast.makeText(
            this,
            "Your measure (${measure.toInt()} dB) was uploaded successfully!",
            Toast.LENGTH_LONG
        )
            .show()
        finish()
    }

}
