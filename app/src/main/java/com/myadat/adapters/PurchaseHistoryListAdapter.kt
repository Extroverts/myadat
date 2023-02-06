package com.myadat.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myadat.R
import com.myadat.databinding.AdapterPurchaseHistoryListBinding
import com.myadat.interfaces.InterfacePurchaseHistory
import com.myadat.model.malModel
import com.myadat.model.purchase
import com.myadat.model.purchaseWithId
import java.lang.reflect.Type


class PurchaseHistoryListAdapter(var context: Context, var list: MutableList<purchaseWithId>,val listener:InterfacePurchaseHistory) :
    RecyclerView.Adapter<PurchaseHistoryListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterPurchaseHistoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = AdapterPurchaseHistoryListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myMalList = list[position]

        holder.binding.tvDate.text="${myMalList.created_at}"
        holder.binding.tvUserNameAndContactDetails.text =
            "${context.getString(R.string.name)}${myMalList.name}\n" +
                    "${context.getString(R.string.contact_no)}${myMalList.user_contact_no}"


        holder.binding.ekun.text =
            "${context.getString(R.string.ekun)}\n ${context.getString(R.string.rupee)} ${myMalList.ekun}"
        holder.binding.ekunDile.text =
            "${context.getString(R.string.ekunDile)}\n ${context.getString(R.string.rupee)} ${myMalList.ekunDile}"
        holder.binding.ekunBaki.text =
            "${context.getString(R.string.ekun_baki)}\n ${context.getString(R.string.rupee)} ${myMalList.ekunBaki}"

        holder.binding.btnShowMalInDetails.setOnClickListener {
            if (holder.binding.rvShowMalDetails.visibility == View.VISIBLE) {
                holder.binding.rvShowMalDetails.visibility = View.GONE
            } else {
                holder.binding.rvShowMalDetails.visibility = View.VISIBLE
                val gson = Gson()
                val jsonOutput = myMalList.mal
                val listType: Type = object : TypeToken<List<malModel?>?>() {}.type
                val posts: List<malModel> = gson.fromJson(jsonOutput, listType)
                Log.d("TAG", "onBindViewHolder: ${posts}")

                holder.binding.malList.layoutManager = LinearLayoutManager(context)
                holder.binding.malList.adapter = MalListAdapter(context, posts.toMutableList())
            }
        }

        holder.binding.deleteData.setOnClickListener {
            Log.d("TAG", "onBindViewHolder: ${myMalList.id}")
            listener.delete(myMalList.id)
        }


        holder.binding.shareData.setOnClickListener {
            listener.share(myMalList.id)
        }


        holder.binding.editData.setOnClickListener {
            listener.edit(myMalList.id)
        }


    }


    override fun getItemCount(): Int {
        return list.size
    }
}