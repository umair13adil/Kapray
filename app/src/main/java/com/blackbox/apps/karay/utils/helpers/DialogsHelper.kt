package com.blackbox.apps.karay.utils.helpers

import android.app.Activity
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import com.blackbox.apps.karay.R
import com.blackbox.apps.karay.models.enums.DialogCallback
import io.reactivex.Observable

object DialogsHelper {

    private var alertDialog: AlertDialog? = null
    private var isDialogShown = false
    private val TAG = "DialogsHelper"


    fun showConfirmDeleteDialog(activity: Activity): Observable<DialogCallback> {

        return createAndShowDialog(
                title = activity.getString(R.string.txt_confirm_delete_post),
                positiveText = activity.getString(R.string.txt_yes),
                negativeText = activity.getString(R.string.txt_no),
                cancelable = true,
                activity = activity
        )
    }

    fun dismissDialog() {
        try {
            if (alertDialog != null) {
                if (alertDialog!!.isShowing) {
                    alertDialog!!.dismiss()
                }
            }
            isDialogShown = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createAndShowDialog(title: String = "",
                                    message: String = "",
                                    negativeText: String = "",
                                    positiveText: String = "",
                                    neutralText: String = "",
                                    cancelable: Boolean = true,
                                    activity: Activity
    ): Observable<DialogCallback> {

        //Flag for indication that action was called by user, not system
        var userCallback = false

        return Observable.create { emitter ->

            if (!activity.isFinishing && !isDialogShown) {

                val alertDialogBuilder = AlertDialog.Builder(activity)

                if (title.isNotEmpty()) {
                    alertDialogBuilder.setTitle(title)
                }

                if (message.isNotEmpty()) {
                    alertDialogBuilder.setMessage(message)
                }

                alertDialogBuilder.setCancelable(cancelable)

                if (negativeText.isNotEmpty()) {
                    alertDialogBuilder.setNegativeButton(negativeText) { dialog, id ->
                        if (!emitter.isDisposed) {
                            emitter.onNext(DialogCallback.NEGATIVE)
                        }

                        //Dismiss Dialog
                        dismissDialog()

                        //Dialog interacted by user
                        userCallback = true
                    }
                }
                if (positiveText.isNotEmpty()) {
                    alertDialogBuilder.setPositiveButton(positiveText) { dialog, id ->
                        if (!emitter.isDisposed) {
                            emitter.onNext(DialogCallback.POSITIVE)
                        }

                        //Dismiss Dialog
                        dismissDialog()

                        //Dialog interacted by user
                        userCallback = true
                    }
                }
                if (neutralText.isNotEmpty()) {
                    alertDialogBuilder.setNeutralButton(neutralText) { dialog, id ->
                        if (!emitter.isDisposed) {
                            emitter.onNext(DialogCallback.NEUTRAL)
                        }

                        //Dismiss Dialog
                        dismissDialog()

                        //Dialog interacted by user
                        userCallback = true
                    }
                }

                alertDialogBuilder.setOnDismissListener {
                    if (!userCallback) {
                        if (!emitter.isDisposed) {
                            emitter.onNext(DialogCallback.DISMISSED)
                        }
                    }
                }

                alertDialogBuilder.setOnCancelListener {
                    if (!userCallback) {
                        if (!emitter.isDisposed) {
                            emitter.onNext(DialogCallback.CANCELLED)
                        }
                    }
                }

                activity.runOnUiThread {
                    alertDialog = alertDialogBuilder.create()
                    alertDialog!!.show()
                    isDialogShown = true
                }
            }
        }
    }

    /**
     * Method to auto dismiss a dialog after a timeout.
     */
    private fun cancelAfterTimeOut(activity: Activity) {
        object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                activity.runOnUiThread {
                    dismissDialog()
                }
            }

            override fun onTick(p0: Long) {

            }

        }.start()
    }
}