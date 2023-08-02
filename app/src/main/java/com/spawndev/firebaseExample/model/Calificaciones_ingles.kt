package com.spawndev.firebaseExample.model

import com.google.gson.annotations.SerializedName

class Calificaciones_ingles {
    @SerializedName("nControl")
    var nControl: Alumnos= Alumnos()

    @SerializedName("grupo")
    var grupo: Grupos= Grupos()

    @SerializedName("nControlAsignaturas")
    var nControlAsignaturas: Asignaturas= Asignaturas()

    @SerializedName("speaking")
    var speaking: Double=0.0

    @SerializedName("reading")
    var reading: Double=0.0

    @SerializedName("listening")
    var listening: Double=0.0

    @SerializedName("writing")
    var writing: Double=0.0

    @SerializedName("use_of_english")
    var use_of_english: Double=0.0

    @SerializedName("promedioIngles")
    var promedioIngles: Double=0.0
}