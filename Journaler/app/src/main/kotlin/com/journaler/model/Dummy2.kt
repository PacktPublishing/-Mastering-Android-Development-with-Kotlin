package com.journaler.model

import android.os.Parcel
import android.os.Parcelable

class Dummy2(
        private var count: Int
) : Parcelable {

    companion object {
        val CREATOR: Parcelable.Creator<Dummy2> = object : Parcelable.Creator<Dummy2> {
            override fun createFromParcel(`in`: Parcel): Dummy2 = Dummy2(`in`)
            override fun newArray(size: Int): Array<Dummy2?> = arrayOfNulls(size)
        }
    }

    private var result: Float = (count * 100).toFloat()

    constructor(`in`: Parcel) : this(`in`.readInt())

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(count)
    }

    override fun describeContents() = 0


}
