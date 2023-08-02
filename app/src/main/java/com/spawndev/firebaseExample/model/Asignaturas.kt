package com.spawndev.firebaseExample.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Asignaturas() : Parcelable {
    @SerializedName("nControlAsignatura")
    var nControlAsignatura: String = ""

    @SerializedName("asignatura")
    var asignatura: String = ""

    constructor(parcel: Parcel) : this() {
        nControlAsignatura = parcel.readString() ?: ""
        asignatura = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nControlAsignatura)
        parcel.writeString(asignatura)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Asignaturas> {
        override fun createFromParcel(parcel: Parcel): Asignaturas {
            return Asignaturas(parcel)
        }

        override fun newArray(size: Int): Array<Asignaturas?> {
            return arrayOfNulls(size)
        }
    }
}