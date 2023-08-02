package com.spawndev.firebaseExample.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Alumnos() : Parcelable {
    @SerializedName("ncontrol")
    var ncontrol: Int = 0

    @SerializedName("nombre")
    var nombre: String = ""

    @SerializedName("grupo")
    var grupo: String = ""

    @SerializedName("estatus")
    var estatus: String = ""

    @SerializedName("email")
    var email: String = ""

    constructor(parcel: Parcel) : this() {
        ncontrol = parcel.readInt()
        nombre = parcel.readString() ?: ""
        grupo = parcel.readString() ?: ""
        estatus = parcel.readString() ?: ""
        email = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ncontrol)
        parcel.writeString(nombre)
        parcel.writeString(grupo)
        parcel.writeString(estatus)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alumnos> {
        override fun createFromParcel(parcel: Parcel): Alumnos {
            return Alumnos(parcel)
        }

        override fun newArray(size: Int): Array<Alumnos?> {
            return arrayOfNulls(size)
        }
    }
}
