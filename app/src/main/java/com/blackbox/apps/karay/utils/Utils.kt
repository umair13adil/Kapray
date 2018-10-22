package com.blackbox.apps.karay.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.location.LocationManager
import android.view.inputmethod.InputMethodManager
import io.reactivex.annotations.NonNull
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        @NonNull
        private val ourInstance = Utils()

        @NonNull
        fun getInstance(): Utils {
            return ourInstance
        }

    }

    fun setTypeface(@NonNull type: Int, @NonNull context: Context): Typeface {

        val assets = context.assets

        var font = Typeface.createFromAsset(assets, "fonts/Roboto-Black.ttf")

        when (type) {
            0 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Black.ttf")
            1 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-BlackItalic.ttf")
            2 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Bold.ttf")
            3 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-BoldItalic.ttf")
            4 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Italic.ttf")
            5 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Light.ttf")
            6 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-LightItalic.ttf")
            7 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Medium.ttf")
            8 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-MediumItalic.ttf")
            9 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Regular.ttf")
            10 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-Thin.ttf")
            11 -> font = Typeface.createFromAsset(assets, "fonts/Roboto-ThinItalic.ttf")
            12 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Bold.ttf")
            13 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-BoldItalic.ttf")
            14 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Italic.ttf")
            15 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Light.ttf")
            16 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-LightItalic.ttf")
            17 -> font = Typeface.createFromAsset(assets, "fonts/RobotoCondensed-Regular.ttf")
        }

        return font
    }

    fun hideKeyboard(context: Context?) {
        try {
            if (context != null) {
                val a = context as Activity?
                val imm = a!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(a.currentFocus!!.windowToken, 0)
            }
        } catch (e: NullPointerException) {

        }
    }

    @NonNull
    fun getSeparatedValues(@NonNull input: String, @NonNull deliminator: String): List<String> {
        val stringList = arrayListOf<String>()

        val v = input.split(deliminator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in v.indices) {
            stringList.add(v[i])
        }

        return stringList
    }


    fun formatDate(date: String): String {
        var dateFormatted = ""
        var spf = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH)
        var newDate: Date? = null
        try {
            newDate = spf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        spf = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
        dateFormatted = spf.format(newDate)
        return "Released: $dateFormatted"
    }

    fun getFormattedDouble(value: Double): Float {
        val precision = Math.pow(10.0, 6.0).toFloat()
        return (precision * value).toInt().toFloat() / precision
    }

    fun isLocationEnabled(context: Context?): Boolean {
        val lm = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return gps_enabled || network_enabled
    }
}