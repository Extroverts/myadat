package com.myadat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import com.myadat.R
import com.myadat.database.SQLDatabase
import com.myadat.databinding.ActivityMainBinding
import com.myadat.retrofit.RetrofitHelper
import com.pixplicity.easyprefs.library.Prefs

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.cVPurchase.setOnClickListener {
            startActivity(Intent(this,PurchaseActivity::class.java))
        }

        binding.cvSell.setOnClickListener {
            startActivity(Intent(this,SellActivity::class.java))
        }

        binding.cvPurchaseHistory.setOnClickListener {
            Prefs.putInt("sell",0)
            startActivity(Intent(this,TotalPurchaseActivity::class.java))
        }


        binding.cVSellHistory.setOnClickListener {
            Prefs.putInt("sell",1)
            startActivity(Intent(this,TotalPurchaseActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateTotal(db = SQLDatabase(this))
    }

    private fun updateTotal(db: SQLDatabase) {
//        binding.totalPurchase.text = "${getString(R.string.history)}\n${getString(R.string.rupee)} ${db.getTotalPurchase()}"
//        binding.totalSell.text = "${getString(R.string.ekunVikri)}\n${getString(R.string.rupee)} ${db.getTotalSell()}"
    }
}