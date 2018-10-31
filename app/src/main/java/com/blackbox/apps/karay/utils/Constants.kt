package com.blackbox.apps.karay.utils

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

        //Search
        const val SEARCH_AND_FILTER = "%filter1%"
        const val CLEAR_SEARCH = "%clear1%"
        const val SEARCH_QUERY_SUBMITTED = "%query1%"

        //Firebase
        val TABLE_REQUESTS = "requests"
        val TABLE_ADMIN_CONTACT = "admin_contact"
        val TABLE_ROUTE = "womenLocal"
        val TABLE_CATEGORY = "category"
        val TABLE_SUB_CATEGORY = "subCategory"
        val TABLE_PHOTOS = "feed_photos"
        val TABLE_LIKE = "like"
        val QUERY_SUCCESS = "200"

        //User Table
        val USER_ROOT = "user"

        //Preferences
        val KEY_LOGIN = "key_login"

        //Images
        val appStoragePath = (Environment.getExternalStorageDirectory().toString() + File.separator + appFolder
                + File.separator)
        val imagesPath = appStoragePath + imagesFolder + File.separator
        val imagesCapturedPath = appStoragePath + imagesFolder + File.separator + imagesCaptured + File.separator
    }
}