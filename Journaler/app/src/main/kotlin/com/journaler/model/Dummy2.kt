package com.journaler.model

import android.os.Parcel
import android.os.Parcelable

class Dummy2 : Parcelable {

    var count: Int = 0
        private set
    var result: Float = 0.toFloat()
        private set

    constructor(count: Int) {
        this.count = count
        this.result = (count * 100).toFloat()
    }

    constructor(`in`: Parcel) {
        count = `in`.readInt()
        result = `in`.readFloat()
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(count)
        parcel.writeFloat(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<Dummy2> = object : Parcelable.Creator<Dummy2> {
            override fun createFromParcel(`in`: Parcel): Dummy2 {
                return Dummy2(`in`)
            }

            override fun newArray(size: Int): Array<Dummy2?> {
                return arrayOfNulls(size)
            }
        }
    }

}
