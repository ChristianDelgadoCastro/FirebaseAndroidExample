package com.spawndev.firebaseExample.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Grupos() : Parcelable {
    @SerializedName("grupo")
    var grupo: String = ""

    @SerializedName("especialidad")
    var especialidad: String = ""

    @SerializedName("activo")
    var activo: Int = 0

    @SerializedName("horario")
    var horario: String = ""

    @SerializedName("turno")
    var turno: String = ""

    constructor(parcel: Parcel) : this() {
        grupo = parcel.readString() ?: ""
        especialidad = parcel.readString() ?: ""
        activo = parcel.readInt()
        horario = parcel.readString() ?: ""
        turno = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(grupo)
        parcel.writeString(especialidad)
        parcel.writeInt(activo)
        parcel.writeString(horario)
        parcel.writeString(turno)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Grupos> {
        override fun createFromParcel(parcel: Parcel): Grupos {
            return Grupos(parcel)
        }

        override fun newArray(size: Int): Array<Grupos?> {
            return arrayOfNulls(size)
        }
    }
}

