package com.example.trackpace.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackpace.adapters.HistoryRecyclerViewAdapter
import com.example.trackpace.databinding.FragmentHistoryBinding
import com.example.trackpace.util.TopSpacingDecoration
import com.example.trackpace.viewmodels.TrackerViewModel


class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryBinding
    lateinit var historyRecyclerViewAdapter: HistoryRecyclerViewAdapter
    private val viewModel: TrackerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(inflater,container,false)
        initRecyclerView()
        viewModel.getRun(historyRecyclerViewAdapter)
        viewModel.runList.observe(viewLifecycleOwner,{
            historyRecyclerViewAdapter.submitList(it.toMutableList())
        })
        return binding.root
    }




    private fun initRecyclerView()
    {
        binding.historyrecView.apply {
            layoutManager= LinearLayoutManager(activity)
            historyRecyclerViewAdapter= HistoryRecyclerViewAdapter()
            val topspace= TopSpacingDecoration(10)
            addItemDecoration(topspace)
            adapter= historyRecyclerViewAdapter
        }
    }




}