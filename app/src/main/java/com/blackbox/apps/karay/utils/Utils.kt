package com.blackbox.apps.karay.utils

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import io.reactivex.annotations.NonNull
import java.io.File


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