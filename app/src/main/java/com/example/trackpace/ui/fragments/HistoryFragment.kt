package com.example.trackpace.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trackpace.R
import com.example.trackpace.databinding.FragmentHistoryBinding
import com.example.trackpace.ui.MainActivity
import com.example.trackpace.viewmodels.TrackerViewModel


class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(inflater,container,false)

        return binding.root
    }




}