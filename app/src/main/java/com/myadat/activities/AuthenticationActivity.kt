package com.myadat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.myadat.R
import com.myadat.databinding.ActivityAuthenticationBinding
import com.myadat.interfaces.InterfaceAuthApi
import com.myadat.model.authResults
import com.myadat.model.sendAuthReq
import com.myadat.retrofit.RetrofitHelper
import com.myadat.utils.Constants
import com.pixplicity.easyprefs.library.Prefs

class AuthenticationActivity : AppCompatActivity(R.layout.activity_authentication) {
    lateinit var binding:ActivityAuthenticationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val string = Secure.getString(contentResolver, Secure.ANDROID_ID)
        binding.tvAndroidId.text = "Unique ID Number:\n $string"

        binding.btnLogin.setOnClickListener {
            if(binding.contactNo.text.toString().length>1){
                startActivity(Intent(this@AuthenticationActivity,MainActivity::class.java))
                Prefs.putBoolean(Constants.IS_USER_LOGGED_IN,true)
                Prefs.putString(Constants.USER_CONTACT_ID,"100")
                finish()
               /* lifecycleScope.launchWhenCreated {
                    val authUserRequest=RetrofitHelper.getInstance().create(InterfaceAuthApi::class.java)
                    val data=sendAuthReq(binding.contactNo.text.toString(),string)
                    val response=authUserRequest.getAuthId(data)
                    if(response.isSuccessful){
                        Log.d("TAG", "onCreate: ${response.body()}")
                        val resp=Gson().toJson(response.body())
                        val api=Gson().fromJson(resp,authResults::class.java)
                        val isActive = api.results[0].is_active
                        val isEndDate=api.results[0].end_at
                        if(isActive=="yes"){
                            startActivity(Intent(this@AuthenticationActivity,MainActivity::class.java))
                            Prefs.putBoolean(Constants.IS_USER_LOGGED_IN,true)
                            Prefs.putString(Constants.USER_CONTACT_ID,binding.contactNo.text.toString())
                            finish()
                        }
                    }else{

                    }
                }*/
            }else{

            }
        }
    }


}