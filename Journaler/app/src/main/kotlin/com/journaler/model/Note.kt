package com.journaler.model

import android.location.Location
import android.os.Parcel
import android.os.Parcelable

class Note(
        title: String,
        message: String,
        location: Location
) : Entry(
        title,
        message,
        location
), Parcelable {

    override var id = 0L

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Location::class.java.classLoader)
    ) {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeParcelable(location, 0)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note = Note(parcel)

        override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
    }

}