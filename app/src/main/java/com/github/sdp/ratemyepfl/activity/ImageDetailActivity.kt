package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.sdp.ratemyepfl.R
import kotlin.math.max
import kotlin.math.min

class ImageDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PHOTO_DISPLAYED: String = "com.github.sdp.extra_image_displayed"
        private const val MIN_SCALE_FACTOR = 0.5f // Zoom out limit
        private const val MAX_SCALE_FACTOR = 5.0f // Zoom in limit
    }

    private var mScaleFactor = 1.0f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor

            // Don't let the image get too small or too large
            mScaleFactor = max(MIN_SCALE_FACTOR, min(mScaleFactor, MAX_SCALE_FACTOR))
            imageView.scaleX = mScaleFactor
            imageView.scaleY = mScaleFactor
            return true
        }
    }

    private lateinit var mScaleDetector: ScaleGestureDetector
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val photoId = intent.getIntExtra(EXTRA_PHOTO_DISPLAYED, 0)

        // The image seen in details
        imageView = findViewById(R.id.detailImageView)
        imageView.setImageResource(photoId)

        // Detects the pinching of the fingers to zoom in/out the image
        mScaleDetector = ScaleGestureDetector(this, scaleListener)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events
        mScaleDetector.onTouchEvent(motionEvent)
        return true
    }

}