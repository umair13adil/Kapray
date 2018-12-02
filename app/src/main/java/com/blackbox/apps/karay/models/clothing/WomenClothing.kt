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
                                                   var file_name: String = "",
                                                   var date_purchased: String = "",
                                                   var date_added: String = "",
                                                   var season_info: String = "",
                                                   var size_info: String = "",
                                                   var price: String = "",
                                                   var kept_away: Boolean = false,
                                                   var un_stitched: Boolean = false,
                                                   var isGift: Boolean = false,
                                                   var userId: String = "",
                                                   var updatedOn: String = "",
                                                   var deletedOn: String = ""
) : RealmObject(), Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(brand_name)
        parcel.writeString(brand_logo_url)
        parcel.writeString(image)
        parcel.writeString(image_url)
        parcel.writeString(file_name)
        parcel.writeString(date_purchased)
        parcel.writeString(date_added)
        parcel.writeString(season_info)
        parcel.writeString(size_info)
        parcel.writeString(price)
        parcel.writeByte(if (kept_away) 1 else 0)
        parcel.writeByte(if (un_stitched) 1 else 0)
        parcel.writeByte(if (isGift) 1 else 0)
        parcel.writeString(userId)
        parcel.writeString(updatedOn)
        parcel.writeString(deletedOn)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "WomenClothing(id='$id', brand_name='$brand_name', brand_logo_url='$brand_logo_url', image='$image', image_url='$image_url', file_name='$file_name', date_purchased='$date_purchased', date_added='$date_added', season_info='$season_info', size_info='$size_info', price='$price', kept_away=$kept_away, un_stitched=$un_stitched, isGift=$isGift, userId='$userId', updatedOn='$updatedOn', deletedOn='$deletedOn')"
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