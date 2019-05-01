package com.example.attendanceapp;

import android.os.Parcel;
import android.os.Parcelable;

public class DanceClass implements Parcelable {
    private String name;

    public DanceClass(String name) {
        this.name = name;
    }
    public DanceClass(){

    }

    protected DanceClass(Parcel in) {
        name = in.readString();
    }

    public static final Creator<DanceClass> CREATOR = new Creator<DanceClass>() {
        @Override
        public DanceClass createFromParcel(Parcel in) {
            return new DanceClass(in);
        }

        @Override
        public DanceClass[] newArray(int size) {
            return new DanceClass[size];
        }
    };

    public String getClassName() {
        return name;
    }

    public void setClassName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
