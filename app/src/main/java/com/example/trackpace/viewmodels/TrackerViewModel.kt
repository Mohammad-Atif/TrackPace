package com.example.trackpace.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class TrackerViewModel:ViewModel() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    var sensorManager:SensorManager? = null

    val loc:MutableLiveData<Location> = MutableLiveData()
    val travelledDistance:MutableLiveData<Float> = MutableLiveData(0f)
    val stepCount:MutableLiveData<Int> = MutableLiveData(0)
    val running:MutableLiveData<Boolean> = MutableLiveData(false)

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
                    calculateDistance(loc.value,location)
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

    private fun calculateDistance(loc1:Location?,loc2:Location){
        if(loc1!=null ){
            val dis=loc1.distanceTo(loc2)
            Log.d("checkDistance","long1:${loc1.longitude} latt1:${loc1.latitude} ")
            Log.d("checkDistance","long2:${loc2.longitude} latt1:${loc2.latitude} ")

            val total_dis=travelledDistance.value!!+dis
            Log.d("checkDistance","distance $total_dis")
            //rounding off the number to 1 digit of decimal value
            val number1digits:Float = String.format("%.1f", total_dis).toFloat()
            travelledDistance.postValue(number1digits)
        }

    }

    //function to start counting the number of steps using android step sensor
    fun startCount(){
        if(running.value==true)
        {
            running.postValue(false)
        }
        else
        {
            running.postValue(true)
            stepCount.postValue(0)
        }

    }


    fun updatestepCount(counts:Int){
        stepCount.postValue(counts)
    }

    

}