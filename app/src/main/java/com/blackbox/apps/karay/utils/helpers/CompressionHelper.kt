package com.blackbox.apps.karay.utils.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.blackbox.apps.karay.models.images.ImageCaptured
import com.blackbox.apps.karay.utils.commons.Constants
import id.zelory.compressor.Compressor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

object CompressionHelper {

    private val TAG = "CompressionHelper"

    fun compressImage(context: Context): Observable<ImageCaptured> {

        return Observable.create {

            val path = Constants.imagesCapturedPath


            val imageFileName = Constants.imagesPath + "img_temp.jpg"

            Compressor(context)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(path)
                    .compressToFileAsFlowable(File(imageFileName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onNext = { file ->

                                try {
                                    deleteLastPhotoTaken(context)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                val file1 = File(imageFileName)
                                if (file1.exists())
                                    file1.delete()

                                val imageName = UUID.randomUUID().toString()

                                val from = File(path, file.name)
                                val to = File(path, imageName)
                                if (from.exists())
                                    from.renameTo(to)

                                val imageCaptured = ImageCaptured()
                                imageCaptured.photoId = imageName
                                imageCaptured.fileName = to.name
                                imageCaptured.filePath = to.path
                                imageCaptured.timeStamp = System.currentTimeMillis() / 1000L

                                it.onNext(imageCaptured)
                            },
                            onError = { throwable ->
                                throwable.printStackTrace()

                                if (!it.isDisposed)
                                    it.onError(throwable)
                            }
                    )
        }
    }

    private fun deleteLastPhotoTaken(context: Context) {
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.MIME_TYPE)

        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")

        if (cursor != null) {
            cursor.moveToFirst()
            val column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val image_path = cursor.getString(column_index_data)
            val file = File(image_path)
            if (file.exists()) {
                file.delete()
            }

            val mediaScanIntent = Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(file) //out is your output file
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }
}