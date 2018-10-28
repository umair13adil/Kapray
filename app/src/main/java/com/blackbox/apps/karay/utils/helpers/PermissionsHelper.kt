package com.blackbox.apps.karay.utils.helpers

import android.Manifest
import android.app.Activity
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.utils.createImageDirectories
import com.blackbox.apps.karay.utils.showToast
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

object PermissionsHelper {

    fun requestStoragePermissions(activity: Activity): Observable<Boolean>  {
        val rxPermissions = RxPermissions(activity)

        return Observable.create { emitter ->
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .compose(rxPermissions.ensureEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                    .subscribeBy { permission ->
                        if (permission.granted) {
                            createImageDirectories()
                        }
                        emitter.onNext(permission.granted)
                    }
        }
    }

    fun requestCameraPermissions(activity: Activity): Observable<Boolean> {
        val rxPermissions = RxPermissions(activity)

        return Observable.create { emitter ->
            rxPermissions.request(Manifest.permission.CAMERA)
                    .compose(rxPermissions.ensureEachCombined(Manifest.permission.CAMERA))
                    .subscribeBy { permission ->
                        if (!permission.granted) {
                            showToast(activity.getString(R.string.error_camera_permissions), activity)
                        }
                        emitter.onNext(permission.granted)
                    }
        }
    }


}