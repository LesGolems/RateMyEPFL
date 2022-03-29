package com.github.sdp.ratemyepfl.activity.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
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
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
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

    private inner class ItemRenderer: DefaultClusterRenderer<RestaurantItem>(applicationContext,
        map,
        mClusterManager) {

        private val mIconGenerator = IconGenerator(applicationContext)
        // private val mClusterIconGenerator = IconGenerator(applicationContext)
        private val mImageView: ImageView = ImageView(applicationContext)
        // private val mClusterImageView: ImageView
        private val mWidth: Int = resources.getDimension(R.dimen.mapItemWidth).toInt()
        private val mHeight: Int = resources.getDimension(R.dimen.mapItemHeight).toInt()

        init {
            // val multiProfile: View = LayoutInflater().inflate(R.layout.multi_profile, null)
            // mClusterIconGenerator.setContentView(multiProfile)
            // mClusterImageView = multiProfile.findViewById(R.id.image)
            mImageView.layoutParams = ViewGroup.LayoutParams(mWidth, mHeight)
            val padding = resources.getDimension(R.dimen.itemPadding).toInt()
            mImageView.setPadding(padding, padding, padding, padding)
            mIconGenerator.setContentView(mImageView)
        }

        override fun onBeforeClusterItemRendered(item: RestaurantItem, markerOptions: MarkerOptions) {
            markerOptions
                .icon(getItemIcon(item))
                .title(item.name)
        }

        override fun onClusterItemUpdated(item: RestaurantItem, marker: Marker) {
            marker.setIcon(getItemIcon(item))
            marker.title = item.name
        }

        override fun onBeforeClusterRendered(@NonNull cluster: Cluster<RestaurantItem>,
                                             markerOptions: MarkerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions)
        }

        override fun onClusterUpdated(@NonNull cluster: Cluster<RestaurantItem>, marker: Marker) {
            super.onClusterUpdated(cluster, marker)
        }

        /**
         * Get a descriptor for a single item from their
         * photo to be used for a marker icon
         *
         * @param item item to return an BitmapDescriptor for
         * @return the item's photo as a BitmapDescriptor
         */
        private fun getItemIcon(item: RestaurantItem): BitmapDescriptor {
            mImageView.setImageResource(item.photo)
            val icon = mIconGenerator.makeIcon()
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        /*
        /**
         * Get a descriptor for multiple people (a cluster) to be used for a marker icon. Note: this
         * method runs on the UI thread. Don't spend too much time in here (like in this example).
         *
         * @param cluster cluster to draw a BitmapDescriptor for
         * @return a BitmapDescriptor representing a cluster
         */

        private fun getClusterIcon(cluster: Cluster<RestaurantItem>): BitmapDescriptor {
            val photos: MutableList<Drawable> = ArrayList(Math.min(4, cluster.getSize()))
            val width = mDimension
            val height = mDimension
            for (i in cluster.items) {
                // Draw 4 at most.
                if (photos.size == 4) break
                val drawable: Drawable = getResources().getDrawable(i.photo)
                drawable.setBounds(0, 0, width, height)
                photos.add(drawable)
            }
            val multiDrawable = MultiDrawable(photos)
            multiDrawable.setBounds(0, 0, width, height)
            mClusterImageView.setImageDrawable(multiDrawable)
            val icon = mClusterIconGenerator.makeIcon(cluster.getSize().toString())
            return BitmapDescriptorFactory.fromBitmap(icon)
        }
        */
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
        /**
        val intent = Intent(this, RestaurantReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, marker.title)
        startActivity(intent)
        */

        return false
    }

    override fun onClusterClick(cluster: Cluster<RestaurantItem>): Boolean {
        return false
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<RestaurantItem>) {
        // Nothing for now
    }

    override fun onClusterItemClick(item: RestaurantItem): Boolean {
        val intent = Intent(this, RestaurantReviewActivity::class.java)
        intent.putExtra(AddReviewActivity.EXTRA_ITEM_REVIEWED, item.name)
        startActivity(intent)

        return false
    }

    override fun onClusterItemInfoWindowClick(item: RestaurantItem) {
        // Nothing for now
    }
}
