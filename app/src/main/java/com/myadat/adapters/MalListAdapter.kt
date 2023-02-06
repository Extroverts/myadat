package com.myadat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myadat.R
import com.myadat.databinding.AdapterMalPurchaseListBinding
import com.myadat.model.malModel

class MalListAdapter(context: Context,var list:MutableList<malModel>): RecyclerView.Adapter<MalListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding:AdapterMalPurchaseListBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =AdapterMalPurchaseListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myMalList=list[position]
        holder.binding.tvNo.text="${position+1}"
        holder.binding.tvMalDesc.text=myMalList.malName
        holder.binding.tvDar.text=myMalList.dar
        holder.binding.tvNag.text=myMalList.nag
        holder.binding.tvVajan.text=myMalList.vajan
        holder.binding.tvTotal.text=myMalList.total
    }

    override fun getItemCount(): Int {
        return list.size
    }
}