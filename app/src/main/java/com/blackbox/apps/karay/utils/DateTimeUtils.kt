package com.blackbox.apps.karay.utils

import android.content.Context
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    var context: Context? = null

    companion object {
        fun create(context: Context): DateTimeUtils = DateTimeUtils(context)
    }

    constructor(context: Context)
    {
        this.context = context
    }

    private val TAG = DateTimeUtils::class.java.simpleName
    private val DATE_FORMAT_DEFAULT = "MM/dd/yyyy"
    private val TIME_FORMAT_DEFAULT = "hh:mm a"
    private val TIME_FORMAT_FULL = "MM:dd:yyyy hh:mm:ss a"

    fun getFullDateString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = sdf.parse(String.format("%2d/%2d/%4d", dayOfMonth, monthOfYear, year))

            val dayNumberSuffix = getDayOfMonthSuffix(dayOfMonth)
            val f1 = SimpleDateFormat("d'$dayNumberSuffix' MMM yyyy", Locale.ENGLISH)
            val formatted = f1.format(date)
            return formatted
        } catch (e: ParseException) {
            // handle exception here !
        }

        return String.format(DATE_FORMAT_DEFAULT, monthOfYear, dayOfMonth, year)
    }

    fun getFullDateTimeString(timestamp: Long): String {
        val date = Date(timestamp)
        val dayNumberSuffix = getDayOfMonthSuffix(date.date)
        val f1 = SimpleDateFormat("d'$dayNumberSuffix' MMMM yyyy hh:mm:ss a", Locale.ENGLISH)
        return f1.format(date)
    }

    fun getFullDateString(timestamp: Long): String {
        val date = Date(timestamp)

        val dayNumberSuffix = getDayOfMonthSuffix(date.date)
        val f1 = SimpleDateFormat("d'$dayNumberSuffix' MMMM yyyy", Locale.ENGLISH)
        val formatted = f1.format(date)
        return formatted
    }

    fun getDateString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date = sdf.parse(String.format("%2d/%2d/%4d", dayOfMonth, monthOfYear, year))

            val f1 = SimpleDateFormat(DATE_FORMAT_DEFAULT, Locale.ENGLISH)
            val formatted = f1.format(date)
            return formatted
        } catch (e: ParseException) {
            // handle exception here !
        }

        return String.format(DATE_FORMAT_DEFAULT, monthOfYear, dayOfMonth, year)
    }

    fun getDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val f1 = SimpleDateFormat(DATE_FORMAT_DEFAULT, Locale.ENGLISH)
        val formatted = f1.format(date)
        return formatted
    }

    fun getTimeString(timestamp: Long): String {
        val date1 = Date(timestamp)
        val f1 = SimpleDateFormat(TIME_FORMAT_DEFAULT, Locale.ENGLISH)
        val formatted = f1.format(date1)
        return formatted
    }

    fun getTimeFormatted(timestamp: Long): String {
        val date1 = Date(timestamp)
        val f1 = SimpleDateFormat(TIME_FORMAT_FULL, Locale.ENGLISH)
        val formatted = f1.format(date1)
        return formatted
    }

    fun getTimeString(hour: Int, minute: Int): String? {
        try {
            val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val date = sdf.parse(String.format("%2s:%2s", hour, minute))

            val f1 = SimpleDateFormat(TIME_FORMAT_DEFAULT, Locale.ENGLISH)
            val formatted = f1.format(date)
            return formatted
        } catch (e: ParseException) {
            // handle exception here !
        }

        return null
    }

    fun getTimestamp(year: Int, month: Int, day: Int): Long {
        try {
            val strDate = String.format("%2d-%2d-%4d", day, month, year)
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = formatter.parse(strDate)
            return date.time
        } catch (e: Exception) {
        }

        return 0
    }

    fun getTimestamp(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        try {
            val strDate = String.format("%2d-%2d-%4d %2d:%2d", day, month, year, hour, minute)
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
            val date = formatter.parse(strDate)
            return date.time
        } catch (e: Exception) {
        }

        return 0
    }

    fun getDayOfMonthSuffix(n: Int): String {
        if (n < 1 || n > 31) {
            throw IllegalArgumentException("Illegal day of month")
        }
        if (n >= 11 && n <= 13) {
            return "th"
        }
        when (n % 10) {
            1 -> return "st"
            2 -> return "nd"
            3 -> return "rd"
            else -> return "th"
        }
    }
}
