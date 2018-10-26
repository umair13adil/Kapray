package com.blackbox.apps.karay.utils.helpers

import android.Manifest
import android.app.Activity
import com.blackbox.apps.karay.utils.createImageDirectories
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.subscribeBy

object PermissionsHelper {

    fun requestStoragePermissions(activity: Activity) {
        val rxPermissions = RxPermissions(activity)
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .compose(rxPermissions.ensureEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribeBy { permission ->
                    if (permission.granted) {
                        createImageDirectories()
                    }
                }
    }
}