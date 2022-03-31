package com.github.sdp.ratemyepfl.fragment.navigation

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.map.MapActivity

class MapFragment : ReviewableTabFragment(R.layout.fragment_map) {
    private lateinit var mapButton: Button
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapButton = view.findViewById<Button>(R.id.mapTabButton).apply {
            setOnClickListener {
                displayContent<MapActivity>()
            }
        }
    }
}