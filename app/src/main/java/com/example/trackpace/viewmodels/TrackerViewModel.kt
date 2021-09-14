package com.example.trackpace.viewmodels

import android.annotation.SuppressLint
import android.app.Application
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
import com.example.trackpace.adapters.HistoryRecyclerViewAdapter
import com.example.trackpace.db.RunDatabase
import com.example.trackpace.models.Run
import com.example.trackpace.repository.RunRepository
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

class TrackerViewModel(
    val runRepository: RunRepository
):ViewModel() {

    val runList:MutableLiveData<List<Run>> = MutableLiveData()

    private lateinit var db:RunDatabase

    fun getRoomInstance(application: Application){
        db= RunDatabase.getInstance(application)
    }

    fun submitRun(run: Run) = viewModelScope.launch {
        runRepository.addRun(run)
        runList.value=runRepository.getRuns()
    }

    fun getRun(historyRecyclerViewAdapter: HistoryRecyclerViewAdapter){

        viewModelScope.launch {
            val list = runRepository.getRuns()
            historyRecyclerViewAdapter.submitList(list.toMutableList())
        }
    }






}