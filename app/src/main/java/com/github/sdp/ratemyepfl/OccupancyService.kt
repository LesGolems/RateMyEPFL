package com.github.sdp.ratemyepfl

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import com.github.sdp.ratemyepfl.database.reviewable.RestaurantRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class OccupancyService : Service(), LocationListener {
    private val precision: Double = 0.0001

    private var insideRestaurantId: String? = null

    private lateinit var locationManager: LocationManager

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var repository: RestaurantRepository

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0f, this)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateRestaurantsOccupancy(l: Location) {
        scope.launch {
            val id = findCloseRestaurantID(l)
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onLocationChanged(location: Location) {
        updateRestaurantsOccupancy(location)
    }
}