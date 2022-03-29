package com.github.sdp.ratemyepfl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoMarkerAdapter(val context: Context, private val onClick: (Marker) -> Unit):  GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val infoWindow: View = LayoutInflater.from(context).inflate(R.layout.cluster_item_window, null)

        val textView: TextView = infoWindow.findViewById(R.id.titleClusterItem)
        textView.text = marker.title

        val reviewButton: Button = infoWindow.findViewById(R.id.reviewableButton)
        reviewButton.setOnClickListener {
            onClick(marker)
        }

        val imageView: ImageView = infoWindow.findViewById(R.id.photoClusterItem)
        imageView.setImageResource(R.drawable.niki)

        return infoWindow
    }
}