package com.example.trackpace.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.trackpace.R
import com.example.trackpace.databinding.FragmentMapBinding
import com.example.trackpace.services.RunningService
import com.example.trackpace.ui.MainActivity
import com.example.trackpace.util.Constants.Companion.zoomLvl
import com.example.trackpace.viewmodels.TrackerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapFragment : Fragment()  {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMapBinding.inflate(inflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        mapFragment?.getMapAsync(this)

        binding.mapView.getMapAsync{
            mMap=it
            drawRoute()
        }


        RunningService.travelledDistance.observe(viewLifecycleOwner, Observer { distance->
            binding.txtDistanceMap.text=distance.toString()
        })

        RunningService.stepCount.observe(viewLifecycleOwner, Observer {steps->
            binding.txtStepsMap.text=steps.toString()
        })

        RunningService.calBurned.observe(viewLifecycleOwner, Observer {cals->
            binding.txtCalBurnedValueMap.text=cals.toString()
        })





    }


    private fun drawRoute()
    {
        RunningService.running.observe(viewLifecycleOwner,{isRunning->

            if(isRunning)
            {
                val loc=RunningService.startLocation.value



                val startLoc = LatLng(loc!!.latitude, loc!!.longitude)
//                mMap.addMarker(MarkerOptions()
//                    .position(startLoc)
//                    .title("Start"))

//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc,zoomLvl))
            }


        })
        val route= PolylineOptions().color(Color.BLUE).width(5f)
        RunningService.LatLngList.observe(viewLifecycleOwner,{
            if(RunningService.running.value!!){
                route.addAll(it)
                mMap.addPolyline(route)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it[it.size-1],zoomLvl))
            }
        })

        // Add a marker in Sydney and move the camera


    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//
//    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onPause()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding.mapView.onDestroy()
//    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}