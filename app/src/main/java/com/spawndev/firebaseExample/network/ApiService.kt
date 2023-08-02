package com.spawndev.firebaseExample.network

import com.spawndev.firebaseExample.model.Calificaciones
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Método para obtener los datos de la tabla "Calificaciones"
    @GET("calificaciones")
    fun obtenerCalificaciones(): Call<List<Calificaciones>>

    // Método para obtener calificaciones por correo electrónico del usuario
    @GET("calificaciones/{email}")
    fun obtenerCalificacionesPorEmail(@Path("email") email: String): Call<List<Calificaciones>>
}