package com.journaler.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dummy2 implements Parcelable {

    private int count;
    private float result;

    public Dummy2(int count) {
        this.count = count;
        this.result = count * 100;
    }

    public Dummy2(Parcel in) {
        count = in.readInt();
        result = in.readFloat();
    }

    public static final Creator<Dummy2> CREATOR = new Creator<Dummy2>() {
        @Override
        public Dummy2 createFromParcel(Parcel in) {
            return new Dummy2(in);
        }

        @Override
        public Dummy2[] newArray(int size) {
            return new Dummy2[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(count);
        parcel.writeFloat(result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return count;
    }

    public float getResult() {
        return result;
    }

}
