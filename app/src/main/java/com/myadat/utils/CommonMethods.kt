package com.myadat.utils

import android.R
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.time.LocalDateTime
import java.time.temporal.ChronoField


class CommonMethods {

    companion object{
        fun hideKeyboard(activity: Activity) {
            val view = activity.findViewById<View>(R.id.content)
            if (view != null) {
                val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


        private fun setDate():String{
            val currentDate= LocalDateTime.now()
            return "${currentDate.get(ChronoField.DAY_OF_MONTH)}/${currentDate.get(ChronoField.MONTH_OF_YEAR)}/${currentDate.get(
                ChronoField.YEAR)}"
        }

    }
}