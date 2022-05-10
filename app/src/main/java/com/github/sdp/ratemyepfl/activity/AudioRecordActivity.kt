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
    private lateinit var recorder: MediaRecorder

    @Volatile
    private var averageIntensity: Double = 0.0

    @Volatile
    private var numberOfMeasures: Int = 0

    @Volatile
    private var start = false

    private lateinit var loopingThread: Thread

    companion object {
        private const val referenceAmplitude = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        // Record to the external cache directory for visibility
        filename = "${externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        recordButton = findViewById(R.id.audioRecordButton)
        recordButton.text = "Start recording"
        recordButton.setOnClickListener {
            start = !start
            onRecord()
        }

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }
    }

    override fun onStop() {
        super.onStop()
        recorder.release()
    }

    private fun onRecord() {
        if (start) {
            recordButton.text = "Stop recording"
            startRecording()
        } else {
            recordButton.text = "Start recording"
            stopRecording()

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
        val maxAmplitude = recorder.maxAmplitude.toDouble()
        return if (maxAmplitude == 0.0) {
            0.0
        } else {
            20 * log10(maxAmplitude / referenceAmplitude)
        }
    }

    private fun startRecording() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setOutputFile(filename)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.prepare()
        recorder.start()

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
        recorder.reset() // You can reuse the object by going back to setAudioSource() step
        recorder.release() // Now the object cannot be reused
    }

}
