package com.lukyanov.vyacheslav.testapp.db.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable

@Entity
data class User(
        @PrimaryKey var id: Int,
        var name: String?,
        var username: String?,
        var email: String?,
        @Embedded var address: UserAddress?,
        var phone: String?,
        var website: String?,
        @Embedded var company: Company?
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(UserAddress::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Company::class.java.classLoader)) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeParcelable(address, flags)
        parcel.writeString(phone)
        parcel.writeString(website)
        parcel.writeParcelable(company, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}