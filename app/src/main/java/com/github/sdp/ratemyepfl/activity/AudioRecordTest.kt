package com.github.sdp.ratemyepfl.activity

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import kotlin.math.log10

open class AudioRecordTest : AppCompatActivity() {

    private lateinit var filename: String

    private lateinit var audioRecordButton: Button
    private lateinit var recorder: MediaRecorder
    private var mStartRecording = true

    private lateinit var audioPlayButton: Button
    private lateinit var player: MediaPlayer
    private var mStartPlaying = true

    companion object {
        private const val referenceAmplitude = 0.00001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)

        // Record to the external cache directory for visibility
        filename = "${externalCacheDir!!.absolutePath}/audiorecordtest.3gp"

        audioRecordButton = findViewById(R.id.audioRecordButton)
        audioRecordButton.setOnClickListener {
            onRecord(mStartRecording)
            audioRecordButton.text = when (mStartRecording) {
                true -> "Stop recording"
                false -> "Start recording"
            }
            mStartRecording = !mStartRecording
        }

        audioPlayButton = findViewById(R.id.audioPlayButton)
        audioPlayButton.setOnClickListener {
            onPlay(mStartPlaying)
            audioPlayButton.text = when (mStartPlaying) {
                true -> "Stop playing"
                false -> "Start playing"
            }
            mStartPlaying = !mStartPlaying
        }

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }

        player = MediaPlayer()
    }

    override fun onStop() {
        super.onStop()
        recorder.release()
        player.release()
    }

    private fun onRecord(start: Boolean) {
        if (start) {
            startRecording()
            for (i in 1..5) {
                val decibels = getAmplitude()
                Toast.makeText(this, "$decibels dB", Toast.LENGTH_SHORT).show()
                Thread.sleep(1000)
            }

        } else {
            stopRecording()
        }
    }

    private fun getAmplitude(): Double {
        val maxAmplitude = recorder.maxAmplitude.toDouble()
        return 20 * log10(maxAmplitude / referenceAmplitude)
    }

    private fun startRecording() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setOutputFile(filename)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        recorder.prepare()
        recorder.start() // Recording is now started
    }

    private fun stopRecording() {
        recorder.reset() // You can reuse the object by going back to setAudioSource() step
        recorder.release() // Now the object cannot be reused
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player.setDataSource(filename)
        player.prepare()
        player.start()
    }

    private fun stopPlaying() {
        player.release()
    }

}
