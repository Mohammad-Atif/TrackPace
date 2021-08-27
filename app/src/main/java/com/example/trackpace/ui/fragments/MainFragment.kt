package com.example.trackpace.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.trackpace.daos.LocationDao
import com.example.trackpace.databinding.FragmentMainBinding
import com.example.trackpace.services.RunningService
import com.example.trackpace.ui.MainActivity
import com.example.trackpace.viewmodels.TrackerViewModel
import com.google.android.gms.location.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: TrackerViewModel by activityViewModels()
    lateinit var  sharedPreference: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMainBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val locationdao=LocationDao()
//        (activity as MainActivity).viewModel.loc.observe(this, Observer {location->
//            val t="latt:${location.latitude} long:${location.longitude}"
//            binding.mainFragtxt.text=t
//        })

        

        sharedPreference= activity?.getSharedPreferences("forcounts",Context.MODE_PRIVATE) ?:return
        val prev_value=sharedPreference.getInt("prev_counts_key",0)

        RunningService.travelledDistance.observe(viewLifecycleOwner, Observer { distance->
            binding.txtDistanceValue.text=distance.toString()
        })

        binding.btnTimer.setOnClickListener {
            if(RunningService.running.value!!) {
                binding.btnTimer.text="Start"
                binding.txtStopwatch.visibility=View.INVISIBLE
            } else {
                binding.btnTimer.text="Stop"
                binding.txtStopwatch.visibility=View.VISIBLE
            }
            RunningService.running.postValue(!RunningService.running.value!!).also {
                RunningService.startcounting()
            }
        }

        RunningService.timePassed.observe(this, Observer {
            binding.txtStopwatch.text=it
        })


        RunningService.stepCount.observe(viewLifecycleOwner, Observer {steps->
            binding.txtStepsValue.text=steps.toString()
        })

        RunningService.calBurned.observe(viewLifecycleOwner, Observer {cals->
            binding.txtCalBurnedValue.text=cals.toString()
        })


    }




}