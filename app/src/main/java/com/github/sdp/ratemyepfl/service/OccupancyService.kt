package com.github.sdp.ratemyepfl.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import com.github.sdp.ratemyepfl.backend.database.reviewable.RestaurantRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

/**
 * Service that handles the occupancy metric for restaurants
 */
@AndroidEntryPoint
class OccupancyService : Service(), LocationListener {
    private val precision: Double = 0.0001

    private var insideRestaurantId: String? = null

    private lateinit var locationManager: LocationManager

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var repository: RestaurantRepository

    /**
     * When the service is started, it subscribes to location updates
     */
    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)
        return super.onStartCommand(intent, flags, startId)
    }


    /**
     * When the location changes, check if the user is inside a restaurant. And update occupancy accordingly
     */
    override fun onLocationChanged(location: Location) {
        scope.launch {
            val id = findCloseRestaurantID(location)
            if (id != insideRestaurantId) {
                insideRestaurantId = if (id == null) {
                    repository.decrementOccupancy(insideRestaurantId!!)
                    null
                } else {
                    repository.incrementOccupancy(id)
                    if (insideRestaurantId != null) {
                        repository.decrementOccupancy(insideRestaurantId!!)
                    }
                    id
                }
            }
        }
    }

    private suspend fun findCloseRestaurantID(l: Location): String? {
        repository.getRestaurants().map { r ->
            if (isClose(l.latitude, r.lat, l.longitude, r.long)) {
                return r.name
            }
        }
        return null
    }

    private fun isClose(lat1: Double, lat2: Double, long1: Double, long2: Double): Boolean {
        return (abs(lat1 - lat2) < precision) && (abs(long1 - long2) < precision)
    }

    /**
     * On service destroy, if the user is inside a restaurant, decrease the occupancy
     */
    override fun onDestroy() {
        super.onDestroy()
        if (insideRestaurantId != null) {
            scope.launch {
                repository.decrementOccupancy(insideRestaurantId!!)
            }
        }
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}