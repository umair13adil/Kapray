package com.blackbox.apps.karay.utils.commons

import android.os.Environment
import java.io.File

/**
 * Created by umair on 12/07/2017.
 */
class Constants {

    companion object {

        //AppFolder
        val appFolder = ".Kapray"
        val imagesFolder = "Images"
        val imagesCaptured = "Captured"

        //Preferences
        val KEY_LOGIN = "key_login"

        //Images
        val appStoragePath = (Environment.getExternalStorageDirectory().toString() + File.separator + appFolder
                + File.separator)
        val imagesPath = appStoragePath + imagesFolder + File.separator
        val imagesCapturedPath = appStoragePath + imagesFolder + File.separator + imagesCaptured + File.separator
    }
}