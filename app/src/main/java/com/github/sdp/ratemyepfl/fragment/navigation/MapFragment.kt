package com.github.sdp.ratemyepfl.fragment.navigation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.map.MapActivity

class MapFragment : Fragment(R.layout.fragment_map) {
    private lateinit var mapButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapButton = view.findViewById<Button>(R.id.mapTabButton).apply {
            setOnClickListener {
                val intent = Intent(activity, MapActivity::class.java)
                startActivity(intent)
            }
        }
    }
}