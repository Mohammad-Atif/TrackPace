package com.example.trackpace.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackpace.adapters.HistoryRecyclerViewAdapter
import com.example.trackpace.databinding.FragmentHistoryBinding
import com.example.trackpace.util.Constants
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreference=activity!!.getSharedPreferences("forcounts", Context.MODE_PRIVATE)


        binding.txtUserNameHistory.text=sharedPreference.getString(Constants.USER_NAME,"null")
    }




    private fun initRecyclerView()
    {
        binding.historyrecView.apply {
            layoutManager= LinearLayoutManager(activity)
            historyRecyclerViewAdapter= HistoryRecyclerViewAdapter()
            val topspace= TopSpacingDecoration(16)
            addItemDecoration(topspace)
            adapter= historyRecyclerViewAdapter
        }
    }




}