package com.blackbox.apps.karay.models.post

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

/**
 * Created by Alessandro on 04/03/2017.
 */
@Keep
class Post()  : Parcelable {

    lateinit var photoAvatar: String

    lateinit var photoCover: String

    lateinit var name: String

    var time: Long? = null

    lateinit var idUser: String

    lateinit var idFeed: String

    lateinit var title: String

    lateinit var text: String

    lateinit var categoryName: String

    lateinit var route: String

    lateinit var points: String

    var feedPhotos: List<String> = ArrayList<String>()

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Post> = object : Parcelable.Creator<Post> {
            override fun createFromParcel(source: Parcel): Post = Post(source)
            override fun newArray(size: Int): Array<Post?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    ) {
        photoAvatar = source.readString()
        photoCover = source.readString()
        name = source.readString()
        time = source.readValue(kotlin.Long::class.java.classLoader) as? Long
        idUser = source.readString()
        idFeed = source.readString()
        title = source.readString()
        text = source.readString()
        categoryName = source.readString()
        route = source.readString()
        points = source.readString()
        feedPhotos = source.createStringArrayList()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(photoCover)
        dest.writeString(photoAvatar)
        dest.writeString(name)
        dest.writeValue(time)
        dest.writeString(idUser)
        dest.writeString(idFeed)
        dest.writeString(title)
        dest.writeString(text)
        dest.writeString(route)
        dest.writeString(categoryName)
        dest.writeString(points)
        dest.writeStringList(feedPhotos)
    }
}
