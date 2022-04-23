package com.github.sdp.ratemyepfl.fragment.navigation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.*
import com.github.sdp.ratemyepfl.utils.MapActivityUtils
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.viewmodel.EventListViewModel
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
class MapFragment : Fragment(R.layout.fragment_map), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener,
    ClusterManager.OnClusterClickListener<MapItem>,
    ClusterManager.OnClusterInfoWindowClickListener<MapItem>,
    ClusterManager.OnClusterItemClickListener<MapItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<MapItem> {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private lateinit var rClusterManager: ClusterManager<MapItem>
    private lateinit var slidingLayout: SlidingUpPanelLayout
    private lateinit var titleView: TextView
    private lateinit var reviewButton: Button
    private lateinit var photoView: ImageView

    private val restaurantViewModel: RestaurantListViewModel by viewModels()
    private val eventListViewModel: EventListViewModel by viewModels()

    /**
     * Renderer of an item in the Restaurant cluster
     */
    private inner class ItemRenderer: DefaultClusterRenderer<MapItem>(activity, map, rClusterManager) {

        override fun onBeforeClusterItemRendered(item: MapItem, markerOptions: MarkerOptions) {
            markerOptions
                .title(item.name)
                .icon(item.icon)
        }

        override fun onClusterItemUpdated(item: MapItem, marker: Marker) {
            marker.title = item.name
            marker.showInfoWindow()
        }

        override fun onBeforeClusterRendered(@NonNull cluster: Cluster<MapItem>,
                                             markerOptions: MarkerOptions
        ) {
            super.onBeforeClusterRendered(cluster, markerOptions)
        }

        override fun onClusterUpdated(@NonNull cluster: Cluster<MapItem>, marker: Marker) {
            super.onClusterUpdated(cluster, marker)
        }

        override fun shouldRenderAsCluster(cluster: Cluster<MapItem>): Boolean {
            return cluster.size > 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        titleView = view.findViewById(R.id.titleClusterItem)
        reviewButton = view.findViewById(R.id.reviewableButton)
        photoView = view.findViewById(R.id.photoClusterItem)
        slidingLayout = view.findViewById(R.id.sliding_map)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        addEPFL()
        initializeClusterManager()
        initializeMap()

        restaurantViewModel.restaurants.observe(this) {
            it?.let { l ->
                listsObserver(l)
            }
        }

        eventListViewModel.events.observe(this) {
            it?.let { l ->
                listsObserver(l)
            }
        }

        enableMyLocation()
    }

    /**
     * Update list of markers
     */
    private fun listsObserver(reviewables: List<Reviewable>) {
        val items = reviewables.map { r ->
            when (r.javaClass) {
                Event::class.java ->
                    EventItem(
                        r as Event,
                        MapActivityUtils.PHOTO_MAPPING.getOrDefault(r.id, R.raw.niki), // Arbitrary default value
                        BitmapDescriptorFactory.fromResource(R.raw.event_marker)
                    )
                else ->
                    RestaurantItem(
                        r as Restaurant,
                        MapActivityUtils.PHOTO_MAPPING.getOrDefault(r.id, R.raw.niki), // Arbitrary default value
                        BitmapDescriptorFactory.fromResource(R.raw.restaurant_marker)
                    )
            }
        }
        addItems(rClusterManager, items)
    }

    /**
     * Initialize the cluster manager
     */
    private fun initializeClusterManager() {
        rClusterManager = ClusterManager(requireContext(), map)
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
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style))

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
     * Add items (restaurants, events) to the cluster
     */
    private fun addItems(clusterManager: ClusterManager<MapItem>, items: List<MapItem>) {
        val markerIds = clusterManager.markerCollection.markers.map { it.id }
        for (i in items) {
            if (!markerIds.contains(i.name)) {
                clusterManager.addItem(i)
            }
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
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "Bringing you home...", Toast.LENGTH_SHORT)
             .show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(
            requireContext(), "Latitude: ${location.latitude}\nLongitude: ${location.longitude}",
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
        if (PermissionUtils.isPermissionGranted(
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

    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            /* Maybe display an error message here */
            permissionDenied = false
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Nothing for now, maybe implement it later
        return false
    }

    override fun onClusterClick(cluster: Cluster<MapItem>): Boolean {
        // Nothing for now, maybe implement it later
        return false
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<MapItem>) {
        // Nothing for now, maybe implement it later
    }

    override fun onClusterItemClick(item: MapItem): Boolean {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        titleView.text = item.name
        reviewButton.setOnClickListener { displayIntent(item) }
        photoView.setImageResource(item.photo)

        map.animateCamera(CameraUpdateFactory.newLatLng(item.position), 250, null)

        return true
    }

    override fun onClusterItemInfoWindowClick(item: MapItem) {
        // Nothing for now, maybe implement it later
    }

    /**
     * Displays the intent launched by the selected cluster item
     */
    private fun displayIntent(item: MapItem) {
        startActivity(item.onClickIntent(activity))
    }
}