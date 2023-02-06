package com.myadat.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.myadat.R
import com.myadat.adapters.PurchaseHistoryListAdapter
import com.myadat.database.SQLDatabase
import com.myadat.databinding.ActivityTotalPurchaseBinding
import com.myadat.interfaces.InterfacePurchaseHistory

class TotalPurchaseActivity : AppCompatActivity(R.layout.activity_total_purchase),InterfacePurchaseHistory {
    lateinit var binding: ActivityTotalPurchaseBinding
    var isSellActivity:Boolean=false
    val db=SQLDatabase(this@TotalPurchaseActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTotalPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launchWhenCreated {
            binding.purchaseList.layoutManager=LinearLayoutManager(this@TotalPurchaseActivity)
            binding.purchaseList.adapter=PurchaseHistoryListAdapter(this@TotalPurchaseActivity,db.getALlPurchaseHistory(),this@TotalPurchaseActivity)

        }
    }

    override fun onResume() {
        super.onResume()
        val intentData= intent.extras?.getBoolean("isSell")
        Log.d("TAG", "onResume: $intentData")
        if(intentData!=null) isSellActivity=intentData
    }

    override fun share(id: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")
        startActivity(sendIntent)
    }

    override fun delete(id: String) {
        db.deleteFromPurchaseById(id)
        binding.purchaseList.adapter?.notifyDataSetChanged()
    }

    override fun edit(id: String) {
        val intent=Intent(this,PurchaseActivity::class.java)
        intent.putExtra("editId",id)
        intent.putExtra("isEdit",true)
        startActivity(intent)
    }
}