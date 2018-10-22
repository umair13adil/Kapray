package com.blackbox.apps.karay.utils

/**
 * Created by umair on 12/07/2017.
 */
class Constants {

    companion object {

        //Search
        const val SEARCH_AND_FILTER = "%filter1%"
        const val CLEAR_SEARCH = "%clear1%"
        const val SEARCH_QUERY_SUBMITTED = "%query1%"

        //Font Types
        val FONT_ROBOTO_BLACK = 0
        val FONT_ROBOTO_BLACK_ITALIC = 1
        val FONT_ROBOTO_BOLD_ITALIC = 3
        val FONT_ROBOTO_ITALIC = 4
        val FONT_ROBOTO_LIGHT = 5
        val FONT_ROBOTO_LIGHT_ITALIC = 6
        val FONT_ROBOTO_MEDIUM = 7
        val FONT_ROBOTO_MEDIUM_ITALIC = 8
        val FONT_ROBOTO_BOLD = 2
        val FONT_ROBOTO_REGULAR = 9
        val FONT_ROBOTO_THIN = 10
        val FONT_ROBOTO_THIN_ITALIC = 11
        val FONT_ROBOTO_CONDENSED_BOLD = 12
        val FONT_ROBOTO_CONDENSED_BOLD_ITALIC = 13
        val FONT_ROBOTO_CONDENSED_ITALIC = 14
        val FONT_ROBOTO_CONDENSED_LIGHT = 15
        val FONT_ROBOTO_CONDENSED_LIGHT_ITALIC = 16
        val FONT_ROBOTO_CONDENSED_REGULAR = 17

        val CLEAR_BACK_STACK = true
        val ADD_TO_BACK_STACK = false
        val REPLACE = true
        val SP_IS_ADMIN = "sp_is_admin"
        val SP_ADMIN_ID = "sp_admin_id"

        val SP_CONTACT_DETAILS = "sp_contact_details"
        val SP_PHONE = "sp_phone"
        val SP_WHATSAPP = "sp_whatsapp"
        val SP_PROFILE_COMPLETE = "sp_profile_complete"
        val SP_USER_PHONE = "sp_user_phone"
        val SP_USER_NAME = "sp_user_name"
        val SP_USER_ID = "sp_user_id"

        //UserLocation Constants
        val SUCCESS_RESULT = 0
        val FAILURE_RESULT = 1
        val PACKAGE_NAME = "com.pakistan.blackbox.electroware"
        val RECEIVER = PACKAGE_NAME + ".RECEIVER"
        val RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY"
        val LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA"

        //Saved Preferences
        val SP_LATITUDE = "sp_latitude"
        val SP_LONGITUDE = "sp_longitude"
        val SP_CITY = "sp_city"
        val SP_COUNTRY = "sp_country"
        val SP_ADDRESS = "sp_address"

        //Firebase
        val TABLE_REQUESTS = "requests"
        val TABLE_ADMIN_CONTACT = "admin_contact"
        val TABLE_ROUTE = "post"
        val TABLE_CATEGORY = "category"
        val TABLE_SUB_CATEGORY = "subCategory"
        val TABLE_PHOTOS = "feed_photos"
        val TABLE_LIKE = "like"
        val QUERY_SUCCESS = "200"

        //User Table
        val USER_ROOT = "user"

        //Preferences
        val KEY_LOGIN = "key_login"
    }
}