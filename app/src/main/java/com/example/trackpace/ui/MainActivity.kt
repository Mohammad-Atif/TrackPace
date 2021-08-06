package com.example.trackpace.ui

import android.Manifest
import android.content.Context
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
import com.example.trackpace.databinding.ActivityMainBinding
import com.example.trackpace.viewmodels.TrackerViewModel
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel=ViewModelProvider(this).get(TrackerViewModel::class.java)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(this,permissions)) {
                viewModel.running.observe(this, Observer {
                    if(it)
                    viewModel.startLocationUpdates(this)
                    else
                        viewModel.stopLocationUpdate()
                })

            } else {
                requestPermissions(arrayOf(permissions), PERMISSION_REQUEST)
            }
        } else {
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient()
//
//            getLocationUpdates()
//            startLocationUpdates()
            viewModel.running.observe(this, Observer {
                if(it)
                    viewModel.startLocationUpdates(this)
                else
                    viewModel.stopLocationUpdate()
            })

        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),SENSOR_REQUEST_CODE)
            }
        }

        sensorManager=getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepsSensor= sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        viewModel.running.observe(this, Observer {
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

        viewModel.running.observe(this, Observer {
            if(it)
                viewModel.startLocationUpdates(this)
            else
                viewModel.stopLocationUpdate()
        })
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
        if (viewModel.running.value==true) {
            viewModel.updatestepCount(event?.values!![0].toInt())
            //stepsValue.setText("" + event.values[0])
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("timer","step counter worked")
    }


}
