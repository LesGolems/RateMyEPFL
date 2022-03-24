package com.github.sdp.ratemyepfl.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.sdp.ratemyepfl.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val epfl = LatLng(46.517, 6.567)
        googleMap.addMarker(
            MarkerOptions()
                .position(epfl)
                .title("Marker at EPFL")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(epfl))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.standardType -> {
            mapFragment.getMapAsync { g -> g.mapType = GoogleMap.MAP_TYPE_NORMAL }
            true
        }
        R.id.satelliteType -> {
            mapFragment.getMapAsync { g -> g.mapType = GoogleMap.MAP_TYPE_SATELLITE }
            true
        }
        R.id.hybridType -> {
            mapFragment.getMapAsync { g -> g.mapType = GoogleMap.MAP_TYPE_HYBRID }
            true
        }
        R.id.terrainType -> {
            mapFragment.getMapAsync { g -> g.mapType = GoogleMap.MAP_TYPE_TERRAIN }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}