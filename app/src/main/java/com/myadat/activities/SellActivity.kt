package com.myadat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.myadat.R
import com.myadat.adapters.MalListAdapter
import com.myadat.database.SQLDatabase
import com.myadat.databinding.ActivitySellBinding
import com.myadat.model.bakiAmt
import com.myadat.model.malModel
import com.myadat.model.sell
import com.myadat.utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList

class SellActivity : AppCompatActivity(R.layout.activity_sell) {
    lateinit var binding: ActivitySellBinding
    lateinit var list:MutableList<malModel>
    lateinit var adapter: MalListAdapter
    lateinit var db:SQLDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySellBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list=ArrayList()
        db= SQLDatabase(this)
        binding.todayDate.setText(setDate())


        binding.custMobile.setEndIconOnClickListener {
            if(binding.custContactNumber.text.toString().length>=10){
                val purchase=db.getBakiAmount(binding.custContactNumber.text.toString())
                binding.magilBaki.text=purchase
                Log.d("TAG", "onCreate: $purchase")
            }
        }


        binding.tvJama.doOnTextChanged { text, start, before, count ->
            try {
                val getMagilBaki=binding.magilBaki.text.toString()
                val updateValue=text.toString().toDouble().minus(getMagilBaki.toDouble())
                binding.shillakAmt.text=updateValue.toString()
            } catch (e: Exception) {
            }

        }

        binding.malList.layoutManager= LinearLayoutManager(this)
        adapter = MalListAdapter(this, list)
        binding.malList.adapter= adapter

        binding.btnAdd.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_purchase_add_mal, null, false)
            val dialog=materialAlertDialogBuilder.create()
            dialog.setView(inflate)
            val malName=inflate.findViewById<AutoCompleteTextView>(R.id.malName)
            val nag=inflate.findViewById<TextInputEditText>(R.id.nag)
            val vajan=inflate.findViewById<TextInputEditText>(R.id.vajan)
            val dar=inflate.findViewById<TextInputEditText>(R.id.dar)
            val total=inflate.findViewById<TextInputEditText>(R.id.total)
            val errorMsg=inflate.findViewById<TextView>(R.id.ErrorMsg)
            val btnSubmit=inflate.findViewById<Button>(R.id.btnSubmit)


            val fruits = arrayOf("गवार" ,"फ्लावर" ,"कोबी" ,"टोमॅटो" ,"मिरची" ,"शिमला", "मिरची" ,"कार्ले" , "आंबट चुका" ," मेथी" , "पालक" , "बटाटा" ,"आद्रक" )
            val adapters = ArrayAdapter(this, android.R.layout.select_dialog_item, fruits)
            malName.threshold = 1;
            malName.setAdapter(adapters);

            malName.doOnTextChanged { text, start, before, count -> if(errorMsg.isVisible)errorMsg.visibility=View.GONE }
            nag.doOnTextChanged { text, start, before, count -> if(errorMsg.isVisible)errorMsg.visibility=View.GONE }
            vajan.doOnTextChanged { text, start, before, count -> if(errorMsg.isVisible)errorMsg.visibility=View.GONE }

            dar.doOnTextChanged { text, start, before, count ->
                try {
                    if(vajan.text.toString().toDouble()>0){
                        val aaa=vajan.text.toString().toDouble() * dar.text.toString().toDouble()
                        total.setText(aaa.toString())
                    }
                } catch (e: Exception) {
                }
                errorMsg.visibility=View.GONE

            }


            btnSubmit.setOnClickListener {
                if(malName.text.toString().isNotEmpty()
                    and nag.text.toString().isNotEmpty()
                    and vajan.text.toString().isNotEmpty()
                    and dar.text.toString().isNotEmpty()) {
                    val aaa = vajan.text.toString().toDouble() * dar.text.toString().toDouble()
                    val total1:Double = String.format("%.2f", aaa).toDouble()
                    list.add(
                        malModel(
                            malName.text.toString(),
                            nag.text.toString(),
                            vajan.text.toString(),
                            dar.text.toString(),
                            total1.toString()
                        )
                    )
                    adapter.notifyItemInserted(list.size - 1)
                    dialog.dismiss()
                    updateTotalValue()
                }else{
                    errorMsg.visibility= View.VISIBLE
                }

            }


            dialog.show()
        }

        binding.btnSave.setOnClickListener {
            val date = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val currentDate = date.format(Date())
            val convertmalToJson = Gson().toJson(list)

            lifecycleScope.launchWhenCreated {
                val sell=sell(
                    Prefs.getString(Constants.USER_CONTACT_ID),
                    binding.custContactNumber.text.toString(),
                    currentDate,
                    binding.custName.text.toString(),
                    convertmalToJson,
                    binding.commissionValue.text.toString(),
                    binding.total.text.toString(),
                    binding.kharcheVaja.text.toString(),
                    binding.ekunDene.text.toString(),
                    binding.tvJama.text.toString(),
                    binding.shillakAmt.text.toString(),"0")

                val bakiAmt = bakiAmt(
                    Prefs.getString(Constants.USER_CONTACT_ID),
                    binding.custContactNumber.text.toString(), binding.shillakAmt.text.toString()
                )
                val db = SQLDatabase(this@SellActivity)
                db.addSell(sell)
                db.addBakiAmount(bakiAmt)
                Toast.makeText(this@SellActivity,"नवीन माल आद्यवत करा केलेला आहे",Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        }


    }

    private fun updateTotalValue() {
        try {
            var total = 0.0
            var minus = 0.0
            var totalnag = 0.0

            for (i in 0 until list.size) {
                total += list[i].total.toDouble()
            }

            for (i in 0 until list.size) {
                totalnag += list[i].nag.toDouble()
            }

            val tot:Double = String.format("%.2f", total).toDouble()
            binding.total.text = tot.toString()

            if (binding.commissionValue.text.toString().toDouble() > 0) {
                minus = total.div(binding.commissionValue.text.toString().toDouble())
                val kharcheVaja:Double = String.format("%.2f", minus).toDouble()
                binding.kharcheVaja.text = kharcheVaja.toString()
                var allTotal = total.plus(minus)
                val tot:Double = String.format("%.2f", allTotal).toDouble()
                binding.ekunDene.text = tot.toString()
            }
        } catch (_: Exception) {

        }
    }


    private fun setDate():String{
        val currentDate=LocalDateTime.now()
        return "${currentDate.get(ChronoField.DAY_OF_MONTH)}/${currentDate.get(ChronoField.MONTH_OF_YEAR)}/${currentDate.get(ChronoField.YEAR)}"
    }
}