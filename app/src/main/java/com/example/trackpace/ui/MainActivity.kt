package com.example.trackpace.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trackpace.R
import com.example.trackpace.databinding.ActivityMainBinding
import com.example.trackpace.services.RunningService
import com.example.trackpace.viewmodels.TrackerViewModel
import android.os.Build.VERSION_CODES.*
import com.example.trackpace.db.RunDatabase
import com.example.trackpace.repository.RunRepository
import com.example.trackpace.viewmodels.TrackerViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    /*
    Start Date 10/07/2021
     */

    /*
    Bug - the step counter start from the starting of the app  not when i click the timer button

     */

    private lateinit var binding: ActivityMainBinding



    private var PERMISSION_REQUEST = 10
    private var SENSOR_REQUEST_CODE = 101
    private var permissions= Manifest.permission.ACCESS_FINE_LOCATION
    lateinit var viewModel: TrackerViewModel
    var sensorManager: SensorManager? = null
    var stepsSensor: Sensor?=null

    @RequiresApi(KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val runRepository=RunRepository(RunDatabase.getInstance(application))
        val trackerViewModelProvider= TrackerViewModelProvider(runRepository)

        viewModel=ViewModelProvider(this,trackerViewModelProvider).get(TrackerViewModel::class.java)



        if (Build.VERSION.SDK_INT >= M) {
            if (checkPermission(this,permissions)) {
                Intent(this,RunningService::class.java).also {
                    startService(it)
                }

            } else {
                requestPermissions(arrayOf(permissions), PERMISSION_REQUEST)
            }
        } else {
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient()
//
//            getLocationUpdates()
//            startLocationUpdates()
            Intent(this,RunningService::class.java).also {
                startService(it)
            }

        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),SENSOR_REQUEST_CODE)
            }
        }

        sensorManager=getSystemService(SENSOR_SERVICE) as SensorManager
        stepsSensor= sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        RunningService.running.observe(this, Observer {
            if(it){
                if(stepsSensor!=null)
                {
                    Log.d("timer","step counter activated")
                   sensorManager?.registerListener(this,
                        stepsSensor,
                        SensorManager.SENSOR_DELAY_UI)
                }
            }
            else{
                if(stepsSensor!=null)
                {
                    Log.d("timer","step counter deactivated")
                    sensorManager?.unregisterListener(this)
                }
            }
        })




        binding.bottomNavigationView.setupWithNavController(navhostFragment.findNavController())




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==PERMISSION_REQUEST){
            if(grantResults[0]== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.finishAffinity(this)
            }
        }
        if(requestCode==SENSOR_REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                Log.d("permission","permission denied")
            }
        }
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity?.applicationContext!!)
//
//        getLocationUpdates()
//        startLocationUpdates()

        Intent(this,RunningService::class.java).also {
            startService(it)
        }
    }



    fun checkPermission(context: Context, permiss: String): Boolean {
        var allSuccess = true
        if (PermissionChecker.checkCallingOrSelfPermission(
                context,
                permiss
            ) == PermissionChecker.PERMISSION_DENIED
        )
            allSuccess = false
        return allSuccess
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("timer","step counter worked")
        if (RunningService.running.value==true) {
            RunningService.stepcountupdate(event?.values!![0].toInt())
            //stepsValue.setText("" + event.values[0])
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("timer","step counter worked")
    }




}
