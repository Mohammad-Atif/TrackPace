package com.example.trackpace.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.lifecycle.Observer
import com.example.trackpace.daos.LocationDao
import com.example.trackpace.databinding.FragmentMainBinding
import com.example.trackpace.ui.MainActivity
import com.example.trackpace.viewmodels.TrackerViewModel
import com.google.android.gms.location.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding


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


    }



}