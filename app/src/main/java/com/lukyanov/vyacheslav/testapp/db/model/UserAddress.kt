package com.lukyanov.vyacheslav.testapp.db.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.os.Parcel
import android.os.Parcelable

data class UserAddress(
        var street: String?,
        var suite: String?,
        var city: String?,
        @ColumnInfo(name = "zip_code") var zipCode: String?,
        @Embedded var geo: Geo?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Geo::class.java.classLoader)) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(street)
        parcel.writeString(suite)
        parcel.writeString(city)
        parcel.writeString(zipCode)
        parcel.writeParcelable(geo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserAddress> {
        override fun createFromParcel(parcel: Parcel): UserAddress {
            return UserAddress(parcel)
        }

        override fun newArray(size: Int): Array<UserAddress?> {
            return arrayOfNulls(size)
        }
    }
}