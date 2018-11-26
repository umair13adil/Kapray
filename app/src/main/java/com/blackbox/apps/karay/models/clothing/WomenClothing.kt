package com.blackbox.apps.karay.models.clothing

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WomenClothing @JvmOverloads constructor(var id: String = "",
                                                   var brand_name: String = "",
                                                   var brand_logo_url: String = "",
                                                   @PrimaryKey var image: String = "",
                                                   var image_url: String = "",
                                                   var date_purchased: String = "",
                                                   var season_info: String = "",
                                                   var kept_away: Boolean = false,
                                                   var un_stitched: Boolean = false,
                                                   var userId: String = ""
) : RealmObject(), Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun toString(): String {
        return "WomenClothing(id=$id, brand_name='$brand_name', image='$image', date_purchased='$date_purchased', season_info='$season_info', kept_away=$kept_away, un_stitched=$un_stitched)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(brand_name)
        parcel.writeString(brand_logo_url)
        parcel.writeString(image)
        parcel.writeString(image_url)
        parcel.writeString(date_purchased)
        parcel.writeString(season_info)
        parcel.writeByte(if (kept_away) 1 else 0)
        parcel.writeByte(if (un_stitched) 1 else 0)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WomenClothing> {
        override fun createFromParcel(parcel: Parcel): WomenClothing {
            return WomenClothing(parcel)
        }

        override fun newArray(size: Int): Array<WomenClothing?> {
            return arrayOfNulls(size)
        }
    }
}