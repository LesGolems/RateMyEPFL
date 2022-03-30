package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

sealed class TabFragment(layout: Int) : Fragment(layout) {
    protected inline fun <reified T : AppCompatActivity> displayContent() {
        val intent = Intent(this.activity, T::class.java)
        startActivity(intent)
    }
}
