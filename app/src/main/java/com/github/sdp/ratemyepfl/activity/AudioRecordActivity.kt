package com.github.sdp.ratemyepfl.activity

import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread
import kotlin.math.log10

@AndroidEntryPoint
open class AudioRecordActivity : AppCompatActivity() {

    private lateinit var filename: String

    private lateinit var recordButton: Button
    private var recorder: MediaRecorder? = null
    private lateinit var loopingThread: Thread

    @Volatile
    private var averageIntensity: Double = 0.0

    @Volatile
    private var numberOfMeasures: Int = 0

    @Volatile
    private var start = false

    companion object {
        private const val referenceAmplitude = 10
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
    }

    /**
     * e.g. when the user quits the Activity while recording
     */
    override fun onPause() {
        super.onPause()
        start = !start
        stopRecording()
    }

    override fun onStop() {
        super.onStop()
        start = !start
        stopRecording()
    }

    private fun onRecord() {
        if (start) {
            recordButton.text = "Stop recording"
            startRecording()
        } else {
            recordButton.text = "Start recording"
            stopRecording()

            // Calculates the average sound intensity that was measured while recording
            // We do not take into account the first measure, which always gives a zero intensity
            if (numberOfMeasures > 0) {
                averageIntensity /= (numberOfMeasures - 1)
            }
            Toast.makeText(this, "$averageIntensity dB (N=$numberOfMeasures)", Toast.LENGTH_SHORT)
                .show()

            averageIntensity = 0.0
            numberOfMeasures = 0
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

}
