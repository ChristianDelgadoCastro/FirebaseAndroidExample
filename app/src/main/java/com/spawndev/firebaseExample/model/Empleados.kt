package com.spawndev.firebaseExample.model

import com.google.gson.annotations.SerializedName

class Empleados {
    @SerializedName("clv_worker")
    var clv_worker: String=""

    @SerializedName("nombre")
    var nombre: String=""

    @SerializedName("password")
    var password: String=""

    @SerializedName("activo")
    var activo: Int=0
}