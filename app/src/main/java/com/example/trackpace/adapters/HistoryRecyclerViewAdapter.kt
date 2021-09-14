package com.example.trackpace.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackpace.R
import com.example.trackpace.models.Run

class HistoryRecyclerViewAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfHistory: MutableList<Run> = mutableListOf<Run>()


    inner class HistoryViewHolder(view: View):RecyclerView.ViewHolder(view){
        val historyDate: TextView = view.findViewById(R.id.TxtHistoryDate)
        val historyDuration: TextView = view.findViewById(R.id.TxtHistoryDuration)
        val historyCals : TextView = view.findViewById(R.id.txtHistoryCal)
        val historyRunImg : ImageView = view.findViewById(R.id.HistoryImg)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.historycardview,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is HistoryViewHolder->{
                holder.historyCals.text= listOfHistory[position].calories
                holder.historyDate.text= listOfHistory[position].date
                holder.historyDuration.text= listOfHistory[position].duration
                Glide.with(holder.itemView).load(listOfHistory[position].imageUri).into(holder.historyRunImg)
            }
        }
    }

    override fun getItemCount(): Int {
       return listOfHistory.size
    }

    fun submitList(list: MutableList<Run>){
        listOfHistory=list
        notifyDataSetChanged()
    }

    fun addtolist(run: Run)
    {
        val l=listOfHistory
        l.add(run)
        listOfHistory=l
        notifyDataSetChanged()
    }

}