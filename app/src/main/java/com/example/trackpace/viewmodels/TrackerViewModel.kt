package com.example.trackpace.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class TrackerViewModel:ViewModel() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    val loc:MutableLiveData<Location> = MutableLiveData()



    /*
    This function will recieve regular location update
    Done throud FusedLocationProviderClient class
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        locationRequest=LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // LocationCallback - Called when FusedLocationProviderClient has a new Location.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)


                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    Log.d("location check","latt:${location.latitude} long:${location.longitude}")
                    loc.postValue(location)
                }


            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    

}