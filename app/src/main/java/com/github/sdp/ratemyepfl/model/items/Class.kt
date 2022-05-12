package com.github.sdp.ratemyepfl.model.items

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Class(
    val id: Int? = null,
    val name: String? = null,
    val teacher: String? = null,
    val room: String? = null,
    val day: Int? = null,
    val start: Int? = null,
    val end: Int? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    fun duration() = (end ?: 0) - (start ?: 0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(teacher)
        parcel.writeString(room)
        parcel.writeValue(day)
        parcel.writeValue(start)
        parcel.writeValue(end)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Class> {
        override fun createFromParcel(parcel: Parcel): Class {
            return Class(parcel)
        }

        override fun newArray(size: Int): Array<Class?> {
            return arrayOfNulls(size)
        }
    }

}
