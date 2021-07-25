package com.example.trackpace.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.trackpace.databinding.ActivityMainBinding
import com.example.trackpace.viewmodels.TrackerViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /*
    Start Date 10/07/2021
     */

    private lateinit var binding: ActivityMainBinding

    private var PERMISSION_REQUEST = 10
    private var permissions= Manifest.permission.ACCESS_FINE_LOCATION
    lateinit var viewModel: TrackerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(TrackerViewModel::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(this,permissions)) {
                viewModel.startTracking(this)
            } else {
                requestPermissions(arrayOf(permissions), PERMISSION_REQUEST)
            }
        } else {
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient()
//
//            getLocationUpdates()
//            startLocationUpdates()
            viewModel.startTracking(this)

        }

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
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity?.applicationContext!!)
//
//        getLocationUpdates()
//        startLocationUpdates()
        viewModel.startTracking(this)
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


}
