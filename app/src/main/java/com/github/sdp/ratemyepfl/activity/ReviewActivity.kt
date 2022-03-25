package com.github.sdp.ratemyepfl.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.Reviewable

class ReviewActivity <T : Reviewable> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_review)
    }
}