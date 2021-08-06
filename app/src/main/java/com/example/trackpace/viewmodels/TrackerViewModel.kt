package com.example.trackpace.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackpace.util.Constants.Companion.USER_AGE
import com.example.trackpace.util.Constants.Companion.USER_GENDER
import com.example.trackpace.util.Constants.Companion.USER_HEIGHT
import com.example.trackpace.util.Constants.Companion.USER_WEIGHT
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.TimeUnit

class TrackerViewModel:ViewModel() {
    private var fusedLocationClient: FusedLocationProviderClient?=null
    private var locationRequest: LocationRequest?=null
    private var locationCallback: LocationCallback?=null

    var sensorManager:SensorManager? = null

    val loc:MutableLiveData<Location> = MutableLiveData()
    val travelledDistance:MutableLiveData<Float> = MutableLiveData(0f)
    val stepCount:MutableLiveData<Int> = MutableLiveData(0)
    val running:MutableLiveData<Boolean> = MutableLiveData(false)
    val calBurned:MutableLiveData<Int> = MutableLiveData(0)
    val currentSpeed:MutableLiveData<Float> = MutableLiveData(0f)
    var prev_count=0
    var total_count=0
    var startTime=0
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

                    Log.d("location check","speed: ${location.speed *3.6f}")
                    currentSpeed.postValue(location.speed*3.6f)   //meter/sec to km/h
                }


            }
        }

        fusedLocationClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdate()
    {
        if(locationCallback!=null && fusedLocationClient!=null)
        fusedLocationClient!!.removeLocationUpdates(locationCallback!!)
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
    fun startCount(context: Context){
        val sharedPreference=context.getSharedPreferences("forcounts",Context.MODE_PRIVATE)
        val prev_value=sharedPreference.getInt("prev_counts_key",0)
        if(running.value==true)
        {
            Log.d("timer","prev value:$prev_value")
            val edit=sharedPreference.edit()
            Log.d("timer","new prev val saving:${total_count}")
            edit.putInt("prev_counts_key",total_count)
            edit.apply()
            edit.commit()
            Toast.makeText(context,"Counter Stopped",Toast.LENGTH_SHORT).show()
            running.postValue(false)
            //startCalorieTracking(sharedPreference)
        }
        else
        {
            Log.d("timer","prev value:$prev_value")
            total_count=prev_value
            prev_count=prev_value
            Toast.makeText(context,"Counter Started",Toast.LENGTH_SHORT).show()
            running.postValue(true).also {
                startCalorieTracking(sharedPreference)
            }

            stepCount.postValue(0)
            travelledDistance.postValue(0f)
            calBurned.postValue(0)
        }

    }


    fun updatestepCount(counts:Int){
        Log.d("timer","update count:$counts")
        total_count=counts
        stepCount.postValue(kotlin.math.abs(counts - prev_count))
    }

    /*
    how to calculate calories burned(formuale)
    -https://www.medicalnewstoday.com/articles/325323#calories-burned-while-walking
    met values - https://www.researchgate.net/figure/Examples-of-MET-values-for-cycling-jogging-and-walking_tbl2_269927658
     */
    private suspend fun calculateCaloriesBurned(weight:Int, height:Int, age:Int, duration:Int, gender:String): Int {
        var bmr=0
        Log.d("caloriesz","corutine function called")
        if(gender=="Male")
        {
            bmr= (66+(6.23*weight)+(12.7*height)-(6.8*age)).toInt()
        }
        else
        {
            bmr= (66+(6.23*weight)+(12.7*height)-(6.8*age)).toInt()
        }

        val met:Float= with(currentSpeed.value!!){
            var newm:Float=0f
            newm=when {
                this<2.0f->{
                    0f
                }
                this<4.0f -> {
                    3.0f
                }
                this<4.8f -> {
                    3.5f
                }
                this<5.6f -> {
                    4.0f
                }
                this<7.2f -> {
                    4.5f
                }
                this<8f -> {
                    8f
                }
                this<9.7f -> {
                    10f
                }
                this<11.3f -> {
                    11.5f
                }
                else -> {
                    13.5f
                }
            }
            newm
        }

        Log.d("calories","met: $met")

        var caloriesBurned=0f

        if(duration!=0)
        {
            caloriesBurned=(bmr*met)/(24*duration)
        }
        caloriesBurned /= 60    //cause we need duration in hour

        return caloriesBurned.toInt()

    }

    //this funtion will use Coroutine to call repeatedly calculate calories burned after delay(1000)
    private fun startCalorieTracking(sharedPreferences: SharedPreferences){
        val weight=sharedPreferences.getInt(USER_WEIGHT,0)
        val age=sharedPreferences.getInt(USER_AGE,0)
        val height=sharedPreferences.getInt(USER_HEIGHT,0)
        val gender=sharedPreferences.getString(USER_GENDER,"null")
        val c=Calendar.getInstance()
        val hour=c.get(Calendar.HOUR_OF_DAY)
        val minute=c.get(Calendar.MINUTE)
        if(gender!="null")
        {
            Log.d("timer","calorieCounter: running ${running.value.toString()}")
            var startVal=true
            viewModelScope.launch {
                while(startVal){
                    val newc=Calendar.getInstance()
                    val currentHour=newc.get(Calendar.HOUR_OF_DAY)
                    val currentMinute=newc.get(Calendar.MINUTE)
                    Log.d("calories","currentMinute:$currentMinute minute:$minute" )
                    val d=(currentHour-hour)*60 + (currentMinute-minute)
                    Log.d("calories","duration d:$d")
                    val cals=calculateCaloriesBurned(weight = weight,
                        height = height,
                        age = age,
                        gender = gender!!,
                    duration = d )
                    Log.d("calories","cals value: $cals")
                    withContext(Main){
                        if(cals>= calBurned.value!!)
                        calBurned.postValue(calBurned.value!!+(cals-calBurned.value!!))
                        else
                            calBurned.postValue(calBurned.value!!+cals)
                    }
                    delay(60000) //1 minute
                    startVal=running.value!!
                }
            }
        }
        else
        {
            Log.d("timer","calorieCounter: Not running")
        }

    }


    

}