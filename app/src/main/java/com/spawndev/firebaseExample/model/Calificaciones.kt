package com.spawndev.firebaseExample.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Calificaciones : Parcelable {
    @SerializedName("nControl")
    var nControl: Alumnos = Alumnos()

    @SerializedName("grupo")
    var grupo: String = ""

    @SerializedName("calificacion")
    var calificacion: Double = 0.0

    @SerializedName("nControlAsignatura")
    var nControlAsignatura: String = ""

    // Nueva propiedad para la asignatura
    var asignatura: Asignaturas? = null

    constructor()

    constructor(parcel: Parcel) {
        nControl = parcel.readParcelable(Alumnos::class.java.classLoader)!!
        grupo = parcel.readString()!!
        calificacion = parcel.readDouble()
        nControlAsignatura = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(nControl, flags)
        parcel.writeString(grupo)
        parcel.writeDouble(calificacion)
        parcel.writeString(nControlAsignatura)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Calificaciones> {
        override fun createFromParcel(parcel: Parcel): Calificaciones {
            return Calificaciones(parcel)
        }

        override fun newArray(size: Int): Array<Calificaciones?> {
            return arrayOfNulls(size)
        }
    }
}
