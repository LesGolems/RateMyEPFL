package com.github.sdp.ratemyepfl.activity.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.AddReviewActivity
import com.github.sdp.ratemyepfl.activity.restaurants.RestaurantReviewActivity
import com.github.sdp.ratemyepfl.model.items.RestaurantItem
import com.github.sdp.ratemyepfl.utils.PermissionUtils.isPermissionGranted
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapActivity : AppCompatActivity(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener,
    ClusterManager.OnClusterClickListener<RestaurantItem>,
    ClusterManager.OnClusterInfoWindowClickListener<RestaurantItem>,
    ClusterManager.OnClusterItemClickListener<RestaurantItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<RestaurantItem> {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private lateinit var mClusterManager: ClusterManager<RestaurantItem>

    private inner class ItemRenderer: DefaultClusterRenderer<RestaurantItem>(applicationContext, map, mClusterManager) {

        override fun onBeforeClusterItemRendered(item: RestaurantItem, markerOptions: MarkerOptions) {
            markerOptions
                .title(item.name)
        }

        override fun onClusterItemUpdated(item: RestaurantItem, marker: Marker) {
            marker.title = item.name
        }

        override fun onBeforeClusterRendered(@NonNull cluster: Cluster<RestaurantItem>,
                                             markerOptions: MarkerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions)
        }

        override fun onClusterUpdated(@NonNull cluster: Cluster<RestaurantItem>, marker: Marker) {
            super.onClusterUpdated(cluster, marker)
        }

        override fun shouldRenderAsCluster(cluster: Cluster<RestaurantItem>): Boolean {
            return cluster.size > 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val epfl = LatLng(46.52, 6.57)
        googleMap.addMarker(
            MarkerOptions()
                .position(epfl)
                .title("EPFL")
        )

        mClusterManager = ClusterManager(this, googleMap)
        mClusterManager.renderer = ItemRenderer()

        googleMap.setOnMarkerClickListener(mClusterManager)
        googleMap.setOnCameraIdleListener(mClusterManager)
        googleMap.setOnInfoWindowClickListener(mClusterManager)
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)

        mClusterManager.setOnClusterClickListener(this)
        mClusterManager.setOnClusterInfoWindowClickListener(this)
        mClusterManager.setOnClusterItemClickListener(this)
        mClusterManager.setOnClusterItemInfoWindowClickListener(this)

        mClusterManager.addItem(RestaurantItem(LatLng(46.519718, 6.564781), "Niki", R.drawable.niki))
        mClusterManager.addItem(RestaurantItem(LatLng(46.518, 6.564), "Niki", R.drawable.niki))

        mClusterManager.cluster()

        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Bringing you home...", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Latitude: ${location.latitude}\nLongitude: ${location.longitude}",
            Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            /* Maybe display an error message here */
            permissionDenied = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.standardType -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.satelliteType -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.hybridType -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.terrainType -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return false
    }

    override fun onClusterClick(cluster: Cluster<RestaurantItem>): Boolean {
        return false
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<RestaurantItem>) {
        // Nothing for now
    }

    override fun onClusterItemClick(item: RestaurantItem): Boolean {
        val titleView: TextView = findViewById(R.id.titleClusterItem)
        titleView.text = item.name
        titleView.visibility = View.VISIBLE

        val reviewButton: Button = findViewById(R.id.reviewableButton)
        reviewButton.setOnClickListener { displayReviews(item) }
        reviewButton.visibility = View.VISIBLE

        val photoView: ImageView = findViewById(R.id.photoClusterItem)
        photoView.setImageResource(item.photo)
        photoView.visibility = View.VISIBLE

        return true
    }

    override fun onClusterItemInfoWindowClick(item: RestaurantItem) {
        // Nothing for now
    }

    fun displayReviews(item: RestaurantItem){
        val intent = Intent(this, RestaurantReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, item.name)
        startActivity(intent)
    }
}
