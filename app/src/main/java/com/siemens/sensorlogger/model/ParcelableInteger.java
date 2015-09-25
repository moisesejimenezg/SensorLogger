package com.siemens.sensorlogger.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moisesjimenez on 9/24/15.
 */
public class ParcelableInteger implements Parcelable{
    private Integer integer;

    public ParcelableInteger(Parcel in){
        readFromParcel(in);
    }

    public ParcelableInteger(int integer){
        this.integer = integer;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(integer);
    }

    public void readFromParcel(Parcel in){
        integer = in.readInt();
    }

    public static final Creator<ParcelableInteger> CREATOR = new Creator<ParcelableInteger>() {
        @Override
        public ParcelableInteger createFromParcel(Parcel source) {
            return new ParcelableInteger(source);
        }

        @Override
        public ParcelableInteger[] newArray(int size) {
            return new ParcelableInteger[size];
        }
    };

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
