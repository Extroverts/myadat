package com.myadat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.myadat.R
import com.myadat.utils.Constants
import com.pixplicity.easyprefs.library.Prefs

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Handler(Looper.myLooper()!!).postDelayed({
            var intent = Intent(this, AuthenticationActivity::class.java)
            if(Prefs.getBoolean(Constants.IS_USER_LOGGED_IN,false)){
                intent = Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        },1500)

    }
}