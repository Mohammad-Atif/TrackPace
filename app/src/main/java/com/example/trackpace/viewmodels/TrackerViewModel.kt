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


//    fun startTracking(context: Context){
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//        getLocationUpdates(context)
//        startLocationUpdates()
//    }
    
    
//    private fun getLocationUpdates()
//    {
//
//
//        locationRequest = LocationRequest()
//        locationRequest.interval = TimeUnit.SECONDS.toMillis(60)
//        locationRequest.fastestInterval = TimeUnit.SECONDS.toMillis(30)
//        locationRequest.smallestDisplacement = 1f // 170 m = 0.1 mile
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult?) {
//                locationResult ?: return
//
//                if (locationResult.locations.isNotEmpty()) {
//                    // get latest location
//                    val location =
//                        locationResult.lastLocation
//                    // use your location object
//                    // get latitude , longitude and other info from this
//                    Log.d("location check","latt:${location.latitude} long:${location.longitude}")
//                    loc.postValue(location)
//                }
//
//
//            }
//        }
//    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        locationRequest=LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

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