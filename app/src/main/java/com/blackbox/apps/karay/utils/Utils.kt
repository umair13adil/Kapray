package com.blackbox.apps.karay.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.util.Base64
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.blackbox.apps.karay.utils.commons.Constants
import io.reactivex.annotations.NonNull
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


fun setTypeface(@NonNull context: Context): Typeface {
    val assets = context.assets
    return Typeface.createFromAsset(assets, "fonts/biysk.ttf")
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

fun createDirIfNotExists(@NonNull path: String): Boolean {
    var ret = true
    val file = File(path)
    if (!file.exists()) {
        if (!file.mkdirs()) {
            ret = false
        }
    }
    return ret
}

fun createImageDirectories() {
    createDirIfNotExists(Constants.appStoragePath)
    createDirIfNotExists(Constants.imagesPath)
    createDirIfNotExists(Constants.imagesCapturedPath)
}

fun showToast(message: String, activity: Activity) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun generateKeyHash(context: Context) {

    // Add code to print out the key hash
    try {
        val info = context.packageManager.getPackageInfo("com.blackbox.apps.karay", PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    } catch (ignored: PackageManager.NameNotFoundException) {
        Log.e("Facebook", ignored.message)
    } catch (ignored: NoSuchAlgorithmException) {
        Log.e("Facebook", ignored.message)
    }

}