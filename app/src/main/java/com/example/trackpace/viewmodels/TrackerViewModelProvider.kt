package com.example.trackpace.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackpace.repository.RunRepository

class TrackerViewModelProvider(
    val runRepository: RunRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackerViewModel(runRepository) as T
    }
}