package com.github.sdp.ratemyepfl.ui.fragment.main

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.github.sdp.ratemyepfl.R
import com.github.sdp.ratemyepfl.model.items.DisplayableOnMap
import com.github.sdp.ratemyepfl.model.items.MapItem
import com.github.sdp.ratemyepfl.utils.MapActivityUtils.getMarkerIconFromDrawable
import com.github.sdp.ratemyepfl.utils.PermissionUtils
import com.github.sdp.ratemyepfl.viewmodel.main.EventListViewModel
import com.github.sdp.ratemyepfl.viewmodel.main.RestaurantListViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMapClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener,
    ClusterManager.OnClusterClickListener<MapItem>,
    ClusterManager.OnClusterInfoWindowClickListener<MapItem>,
    ClusterManager.OnClusterItemClickListener<MapItem>,
    ClusterManager.OnClusterItemInfoWindowClickListener<MapItem> {

    private var permissionDenied = true
    private lateinit var map: GoogleMap
    private lateinit var rClusterManager: ClusterManager<MapItem>
    private lateinit var slidingLayout: SlidingUpPanelLayout
    private lateinit var titleView: TextView
    private lateinit var reviewButton: Button
    private lateinit var photoView: ImageView

    private var onClickMarker: Marker? = null
    val onClickLocation = MutableLiveData<LatLng>()

    private val restaurantViewModel: RestaurantListViewModel by viewModels()
    private val eventListViewModel: EventListViewModel by viewModels()

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>

    /**
     * Renderer of an item in the Restaurant cluster
     */
    private inner class ItemRenderer :
        DefaultClusterRenderer<MapItem>(activity, map, rClusterManager) {

        override fun onBeforeClusterItemRendered(item: MapItem, markerOptions: MarkerOptions) {
            markerOptions
                .title(item.name)
                .icon(getMarkerIconFromDrawable(resources, item.icon))
        }

        override fun onClusterItemUpdated(item: MapItem, marker: Marker) {
            marker.title = item.name
            marker.showInfoWindow()
        }

        override fun onBeforeClusterRendered(
            @NonNull cluster: Cluster<MapItem>,
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

        @SuppressLint("MissingPermission")
        locationPermissionLauncher = PermissionUtils.requestPermissionLauncher(
            {
                map.isMyLocationEnabled = true
                permissionDenied = false
            }, this, requireContext()
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        addEPFL()
        initializeClusterManager()
        initializeMap()

        restaurantViewModel.elements.observe(this) {
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
    private fun listsObserver(disp: List<DisplayableOnMap>) {
        val items = disp.map { d -> d.toMapItem() }
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
        map.setOnMapClickListener(this)
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style))

        onClickMarker = map.addMarker(
            MarkerOptions()
                .position(LatLng(0.0, 0.0))
                .icon(getMarkerIconFromDrawable(resources, R.drawable.ic_location_dot_solid))
        )
        onClickMarker?.isVisible = false
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
                .icon(getMarkerIconFromDrawable(resources, R.drawable.ic_school_solid_small))
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
        if (slidingLayout.panelState != SlidingUpPanelLayout.PanelState.COLLAPSED &&
                slidingLayout.panelState != SlidingUpPanelLayout.PanelState.HIDDEN) {
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }
    }

    /**
     * Enable location tracking if permissions has already been granted, else request permissions
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        PermissionUtils.verifyPermissionAndExecute(
            {
                map.isMyLocationEnabled = true
                permissionDenied = false
            },
            locationPermissionLauncher,
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
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

    override fun onStart() {
        super.onStart()
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
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
        titleView.text = item.name
        reviewButton.setOnClickListener { displayIntent(item) }
        photoView.setImageResource(item.photo)
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        map.animateCamera(CameraUpdateFactory.newLatLng(item.position), 250, null)

        return true
    }

    override fun onClusterItemInfoWindowClick(item: MapItem) {
        // Nothing for now, maybe implement it later
    }

    override fun onMapClick(location: LatLng) {
        collapsePanel()
        onClickLocation.postValue(location)
        onClickMarker?.position = location
        onClickMarker?.isVisible = true
    }

    /**
     * Displays the intent launched by the selected cluster item
     */
    private fun displayIntent(item: MapItem) {
        startActivity(item.onClickIntent(activity))
    }
}