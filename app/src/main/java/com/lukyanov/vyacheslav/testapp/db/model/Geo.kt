package com.lukyanov.vyacheslav.testapp.db.model

import android.os.Parcel
import android.os.Parcelable

data class Geo(
    var lat: Double?,
    var lng: Double?): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lat)
        parcel.writeValue(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Geo> {
        override fun createFromParcel(parcel: Parcel): Geo {
            return Geo(parcel)
        }

        override fun newArray(size: Int): Array<Geo?> {
            return arrayOfNulls(size)
        }
    }
}