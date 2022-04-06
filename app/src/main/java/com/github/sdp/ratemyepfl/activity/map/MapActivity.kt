package com.github.sdp.ratemyepfl.activity.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.activity.ReviewActivity
import com.github.sdp.ratemyepfl.model.items.Restaurant
import com.github.sdp.ratemyepfl.model.items.RestaurantItem
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.github.sdp.ratemyepfl.utils.PermissionUtils.isPermissionGranted
import com.github.sdp.ratemyepfl.viewmodel.RestaurantListViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
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
    private lateinit var rClusterManager: ClusterManager<RestaurantItem>
    private lateinit var slidingLayout: SlidingUpPanelLayout
    private lateinit var titleView: TextView
    private lateinit var reviewButton: Button
    private lateinit var photoView: ImageView

    private val restaurantViewModel: RestaurantListViewModel by viewModels()

    /**
     * Renderer of an item in the Restaurant cluster
     */
    private inner class ItemRenderer: DefaultClusterRenderer<RestaurantItem>(applicationContext, map, rClusterManager) {

        override fun onBeforeClusterItemRendered(item: RestaurantItem, markerOptions: MarkerOptions) {
            markerOptions
                .title(item.name)
                .icon(BitmapDescriptorFactory.fromResource(R.raw.restaurant_marker))
        }

        override fun onClusterItemUpdated(item: RestaurantItem, marker: Marker) {
            marker.title = item.name
            marker.showInfoWindow()
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

        titleView = findViewById(R.id.titleClusterItem)
        reviewButton = findViewById(R.id.reviewableButton)
        photoView = findViewById(R.id.photoClusterItem)
        slidingLayout = findViewById(R.id.sliding_map)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        addEPFL()
        initializeClusterManager()
        initializeMap()

        restaurantViewModel.restaurants.observe(this) {
            it?.let {
                addRestaurants(rClusterManager, it)
            }
        }

        enableMyLocation()
    }

    /**
     * Initialize the cluster manager
     */
    private fun initializeClusterManager() {
        rClusterManager = ClusterManager(this, map)
        rClusterManager.renderer = ItemRenderer()
        rClusterManager.setOnClusterClickListener(this)
        rClusterManager.setOnClusterInfoWindowClickListener(this)
        rClusterManager.setOnClusterItemClickListener(this)
        rClusterManager.setOnClusterItemInfoWindowClickListener(this)
    }

    /**
     * Initialize the map
     */
    private fun initializeMap() {
        map.setOnCameraIdleListener(rClusterManager)
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style))

        map.setOnMapClickListener {
            collapsePanel()
        }
    }

    /**
     * Add the EPFL marker to the map
     */
    private fun addEPFL() {
        val epfl = LatLng(46.52, 6.57)
        map.addMarker(
            MarkerOptions()
                .position(epfl)
                .title("EPFL")
                .icon(BitmapDescriptorFactory.fromResource(R.raw.school_marker))
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(epfl, 16f))
    }

    /**
     * Add restaurants from the database to the cluster
     */
    private fun addRestaurants(clusterManager: ClusterManager<RestaurantItem>, restaurants: List<Restaurant>) {
        clusterManager.clearItems()
        for (r in restaurants) {
            clusterManager.addItem(
                RestaurantItem(
                    LatLng(r.lat, r.long),
                    r.id,
                    MapActivityUtils.PHOTO_MAPPING.getOrDefault(r.id, R.raw.niki)) // Arbitrary default value
            )
        }
        clusterManager.cluster()
    }

    /**
     * Collapse sliding panel if expanded
     */
    private fun collapsePanel() {
        if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    /**
     * Enable location tracking if permissions has already been granted, else request permissions
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
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
        Toast.makeText(
            this, "Latitude: ${location.latitude}\nLongitude: ${location.longitude}",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
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
        // Nothing for now, maybe implement it later
        return false
    }

    override fun onClusterClick(cluster: Cluster<RestaurantItem>): Boolean {
        // Nothing for now, maybe implement it later
        return false
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<RestaurantItem>) {
        // Nothing for now, maybe implement it later
    }

    override fun onClusterItemClick(item: RestaurantItem): Boolean {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        titleView.text = item.name
        reviewButton.setOnClickListener { displayReviews(item) }
        photoView.setImageResource(item.photo)

        return true
    }

    override fun onClusterItemInfoWindowClick(item: RestaurantItem) {
        // Nothing for now, maybe implement it later
    }

    /**
     * Displays the reviews of the selected cluster item
     */
    private fun displayReviews(item: RestaurantItem) {
        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_ITEM_REVIEWED, item.name)
        intent.putExtra(ReviewActivity.EXTRA_LAYOUT_ID, R.layout.activity_restaurant_review) // Will have to change later
        startActivity(intent)
    }
}
