package com.blackbox.apps.karay.utils.helpers

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.blackbox.apps.karay.utils.commons.Constants
import com.blackbox.apps.karay.utils.createImageDirectories
import java.io.File
import java.io.IOException

object ImageCaptureHelper{

    fun takePicture(activity: Activity, requestCode: Int) {

        //Delete Previous File if any
        val imageFileName = Constants.imagesPath + "img_temp.jpg"
        val file = File(imageFileName)
        if (file.exists())
            file.delete()

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {

            var photoFile: File? = null
            try {
                createImageDirectories()
                photoFile = File(imageFileName)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity, "com.blackbox.apps.karay.provider", photoFile)
                takePictureIntent.putExtra("android.intent.extra.quickCapture", true)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }
}