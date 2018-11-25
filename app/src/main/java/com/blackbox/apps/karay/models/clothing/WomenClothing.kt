package com.blackbox.apps.karay.models.clothing

import io.realm.RealmObject

open class WomenClothing(
        /*@PrimaryKey*/ var id_local: Int = 0,
        var brand_name: String = "",
        var brand_logo_url: String = "",
        var image: String = "",
        var image_url: String = "",
        var date_purchased: String = "",
        var season_info: String = "",
        var kept_away: Boolean = false,
        var un_stitched: Boolean = false
) : RealmObject() {

    override fun toString(): String {
        return "WomenClothing(id_local=$id_local, brand_name='$brand_name', image='$image', date_purchased='$date_purchased', season_info='$season_info', kept_away=$kept_away, un_stitched=$un_stitched)"
    }
}