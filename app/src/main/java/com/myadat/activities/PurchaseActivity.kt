package com.myadat.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myadat.R
import com.myadat.adapters.MalListAdapter
import com.myadat.database.SQLDatabase
import com.myadat.databinding.ActivityPurchaseBinding
import com.myadat.model.bakiAmt
import com.myadat.model.malModel
import com.myadat.model.purchase
import com.myadat.utils.CommonMethods
import com.myadat.utils.Constants
import com.pixplicity.easyprefs.library.Prefs
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


class PurchaseActivity : AppCompatActivity(R.layout.activity_purchase) {
    lateinit var binding: ActivityPurchaseBinding
    lateinit var list: MutableList<malModel>
    lateinit var adapter: MalListAdapter
    var editId: String = "0"
    var isEdit:Boolean=false
    val db = SQLDatabase(this@PurchaseActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = ArrayList()
        try {
            editId = intent.getStringExtra("editId").toString()
            isEdit=intent.getBooleanExtra("isEdit",false)
        } catch (e: Exception) {

        }

        if (isEdit) {

            val purchase = db.getPurchaseById(editId)

            binding.custName.setText(purchase.name)
            binding.custContactNumber.setText(purchase.user_contact_no)

            val gson = Gson()
            val jsonOutput = purchase.mal
            val listType: Type = object : TypeToken<List<malModel?>?>() {}.type
            val posts: MutableList<malModel> = gson.fromJson(jsonOutput, listType)
            list=posts

            binding.commissionValue.setText(purchase.commission_percent)
            binding.aadat.setText(purchase.aadat)
            binding.hamali.setText(purchase.hamali)
            binding.tolai.setText(purchase.tolai)
            binding.uchal.setText(purchase.uchal)
            binding.kameti.setText(purchase.kameti)
            binding.bhade.setText(purchase.bhade)
            binding.badla.setText(purchase.badla)
            binding.total.text = purchase.rakkam
            binding.kharcheVaja.text = purchase.kharche
            binding.ekunDene.text = purchase.ekun
            binding.magilRakkam.text = purchase.ekunBaki
            binding.tvDile.setText(purchase.ekunDile)

        }


        binding.malList.layoutManager = LinearLayoutManager(this)
        adapter = MalListAdapter(this, list)
        binding.malList.adapter = adapter

        binding.commissionValue.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.aadat.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.hamali.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.tolai.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.uchal.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.kameti.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.bhade.doOnTextChanged { text, start, before, count -> updateTotalValue() }
        binding.badla.doOnTextChanged { text, start, before, count -> updateTotalValue() }

        binding.custContactNumber.doOnTextChanged { text, start, before, count ->
            Log.d("TAG", "onCreate: $count")
            if (binding.custContactNumber.text?.length == 10) {
                binding.magilRakkam.text =
                    db.getBakiAmount(binding.custContactNumber.text.toString())
            }
        }



        binding.tvDile.doOnTextChanged { text, start, before, count ->
            try {
                val getTotal = binding.ekunDene.text.toString().toDouble()
                val minus = getTotal.minus(text.toString().toDouble())
                val lastBakiRakkam = binding.magilRakkam.text.toString().toDouble()
                val ramains = getTotal.minus(lastBakiRakkam)
                val data = ramains - minus
                binding.remainingAmt.text = data.toString()
            } catch (e: Exception) {

            }

        }


        binding.custMobile.setEndIconOnClickListener {
//            importContact()
        }

        binding.btnAdd.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            val inflate =
                LayoutInflater.from(this).inflate(R.layout.dialog_purchase_add_mal, null, false)
            val dialog = materialAlertDialogBuilder.create()
            dialog.setView(inflate)
            val malName = inflate.findViewById<AutoCompleteTextView>(R.id.malName)
            val nag = inflate.findViewById<TextInputEditText>(R.id.nag)
            val vajan = inflate.findViewById<TextInputEditText>(R.id.vajan)
            val dar = inflate.findViewById<TextInputEditText>(R.id.dar)
            val total = inflate.findViewById<TextInputEditText>(R.id.total)
            val errorMsg = inflate.findViewById<TextView>(R.id.ErrorMsg)
            val btnSubmit = inflate.findViewById<Button>(R.id.btnSubmit)


            val fruits =
                arrayOf(
                    "गवार",
                    "फ्लावर",
                    "कोबी",
                    "टोमॅटो",
                    "मिरची",
                    "शिमला",
                    "मिरची",
                    "कार्ले",
                    "आंबट चुका",
                    "मेथी",
                    "पालक",
                    "बटाटा",
                    "आद्रक"
                )
            val adapters = ArrayAdapter(this, android.R.layout.select_dialog_item, fruits)
            malName.threshold = 1;
            malName.setAdapter(adapters);

            malName.doOnTextChanged { text, start, before, count ->
                if (errorMsg.isVisible) errorMsg.visibility =
                    View.GONE
            }
            nag.doOnTextChanged { text, start, before, count ->
                if (errorMsg.isVisible) errorMsg.visibility =
                    View.GONE
            }
            vajan.doOnTextChanged { text, start, before, count ->
                if (errorMsg.isVisible) errorMsg.visibility =
                    View.GONE
            }

            dar.doOnTextChanged { text, start, before, count ->
                try {
                    if (vajan.text.toString().toDouble() > 0) {
                        val aaa = vajan.text.toString().toDouble() * dar.text.toString().toDouble()
                        total.setText(aaa.toString())
                    }
                } catch (e: Exception) {
                }
                errorMsg.visibility = View.GONE

            }


            btnSubmit.setOnClickListener {
                if (malName.text.toString().isNotEmpty()
                    and nag.text.toString().isNotEmpty()
                    and vajan.text.toString().isNotEmpty()
                    and dar.text.toString().isNotEmpty()
                ) {
                    val aaa = vajan.text.toString().toDouble() * dar.text.toString().toDouble()
                    list.add(
                        malModel(
                            malName.text.toString(),
                            nag.text.toString(),
                            vajan.text.toString(),
                            dar.text.toString(),
                            aaa.toString()
                        )
                    )
                    adapter.notifyItemInserted(list.size - 1)
                    dialog.dismiss()
                    updateTotalValue()
                    CommonMethods.hideKeyboard(this)
                } else {
                    errorMsg.visibility = View.VISIBLE
                }
            }
            dialog.show()
        }





        binding.btnSave.setOnClickListener {
            if (binding.custName.text.toString().isEmpty()) {
                Toast.makeText(this, "कृपया नाव टाका", Toast.LENGTH_SHORT).show()
            } else if (binding.custContactNumber.text.toString().length < 10) {
                Toast.makeText(this, "मोबाईल नंबर १० अंकी टाका ", Toast.LENGTH_SHORT).show()
            } else if (list.size < 1) {
                Toast.makeText(this, "कृपया नवीन माल आद्यवत करा ", Toast.LENGTH_SHORT).show()
            } else if (binding.tvDile.text.toString().toInt() < 1) {
                Toast.makeText(this, "एकूण दिले हि रक्कम टाका ", Toast.LENGTH_SHORT).show()
            } else {
                val date = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
                val currentDate = date.format(Date())
                val convertmalToJson = Gson().toJson(list)
                lifecycleScope.launchWhenCreated {

                    val inserData = purchase(
                        Prefs.getString(Constants.USER_CONTACT_ID),
                        binding.custContactNumber.text.toString(),
                        currentDate,
                        binding.custName.text.toString(),
                        convertmalToJson,
                        binding.commissionValue.text.toString(),
                        binding.aadat.text.toString(),
                        binding.tolai.text.toString(),
                        binding.hamali.text.toString(),
                        binding.kameti.text.toString(),
                        binding.uchal.text.toString(),
                        binding.bhade.text.toString(),
                        binding.badla.text.toString(),
                        binding.total.text.toString(),
                        binding.kharcheVaja.text.toString(),
                        binding.ekunDene.text.toString(),
                        binding.tvDile.text.toString(),
                        binding.remainingAmt.text.toString()
                    )

                    val bakiAmt = bakiAmt(
                        Prefs.getString(Constants.USER_CONTACT_ID),
                        binding.custContactNumber.text.toString(),
                        binding.remainingAmt.text.toString()
                    )
                    val db = SQLDatabase(this@PurchaseActivity)
                    if(isEdit){
                        db.addOrUpdatePurchase(inserData,editId)
                    }else{
                        db.addOrUpdatePurchase(inserData)
                    }

                    db.addBakiAmount(bakiAmt)
//                    db.addBakiAmount(bakiAmt)

                    Toast.makeText(
                        this@PurchaseActivity,
                        "नवीन माल आद्यवत करा केलेला आहे",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                }
//val authUserRequest = RetrofitHelper.getInstance().create(InterfaceAuthApi::class.java)
                //                val response=authUserRequest.insertPurchaseRecord(inserData)
//                if(response.isSuccessful){
//                    Log.d("TAG", "onCreate: successful")
//                }


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


            binding.total.text = total.toString()

            if (binding.commissionValue.text.toString().toInt() > 0) {
                minus = totalnag * binding.commissionValue.text.toString().toInt()
                binding.kharcheVaja.text = minus.toString()
                var allTotal = total.minus(minus)
                allTotal -= binding.aadat.text.toString().toInt()
                allTotal -= binding.tolai.text.toString().toInt()
                allTotal -= binding.hamali.text.toString().toInt()
                allTotal -= binding.uchal.text.toString().toInt()
                allTotal -= binding.kameti.text.toString().toInt()
                allTotal -= binding.bhade.text.toString().toInt()
                allTotal -= binding.badla.text.toString().toInt()
                val number2digits: Double = String.format("%.2f", allTotal).toDouble()
                binding.ekunDene.text = number2digits.toString()
            }
        } catch (_: Exception) {

        }
    }

}