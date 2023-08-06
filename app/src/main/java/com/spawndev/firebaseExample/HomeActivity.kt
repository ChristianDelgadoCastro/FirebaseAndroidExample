package com.spawndev.firebaseExample

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.spawndev.firebaseExample.model.Asignaturas
import com.spawndev.firebaseExample.model.Calificaciones
import com.spawndev.firebaseExample.network.ApiService
import com.spawndev.firebaseExample.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

enum class ProviderType {
    BASIC,
    GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var providerTextView: TextView
    private lateinit var logOutButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var calificacionesAdapter: CalificacionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Referencias de las vistas
        emailTextView = findViewById(R.id.emailTextView)
        providerTextView = findViewById(R.id.providerTextView)
        logOutButton = findViewById(R.id.logOutButton)
        //Configuración del RecyclerView y el ItemDecoration
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        calificacionesAdapter = CalificacionesAdapter()
        recyclerView.adapter = calificacionesAdapter

        // Después de configurar el layoutManager y el adapter para el RecyclerView
        val verticalSpacing = resources.getDimensionPixelSize(R.dimen.vertical_spacing)
        val horizontalSpacing = resources.getDimensionPixelSize(R.dimen.horizontal_spacing)
        //Distribucion del recyclerView
        val itemDecoration = CustomItemDecoration(verticalSpacing, horizontalSpacing)
        recyclerView.addItemDecoration(itemDecoration)

        // Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?:"")
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.obtenerCalificacionesPorEmail(email ?: "")

        // Guardado de datos
        val prefs : SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE).edit()
        prefs?.putString("email", email)
        prefs?.putString("provider", provider)
        prefs?.apply()

        call.enqueue(object : Callback<List<Calificaciones>> {
            override fun onResponse(call: Call<List<Calificaciones>>, response: Response<List<Calificaciones>>) {
                if (response.isSuccessful) {
                    val calificacionesList = response.body()

                    // Obtener nombres de asignaturas
                    val asignaturasCall = apiService.obtenerAsignaturas()

                    asignaturasCall.enqueue(object : Callback<List<Asignaturas>> {
                        override fun onResponse(call: Call<List<Asignaturas>>, response: Response<List<Asignaturas>>) {
                            if (response.isSuccessful) {
                                val asignaturasList = response.body()

                                // Asociar asignaturas a calificaciones
                                val calificacionesConAsignaturas = mutableListOf<Calificaciones>()
                                calificacionesList?.forEach { calificacion ->
                                    val asignatura = asignaturasList?.find { it.nControlAsignatura == calificacion.nControlAsignatura }
                                    if (asignatura != null) {
                                        calificacion.asignatura = asignatura
                                        calificacionesConAsignaturas.add(calificacion)
                                    }
                                }

                                // Actualizar el adaptador con la lista de calificaciones y asignaturas
                                calificacionesAdapter.setCalificacionesList(calificacionesConAsignaturas)
                            } else {
                                // Manejar el caso de respuesta fallida
                                Toast.makeText(this@HomeActivity, "Error al obtener las asignaturas", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Asignaturas>>, t: Throwable) {
                            // Manejar el caso de fallo en la solicitud
                            Toast.makeText(this@HomeActivity, "Error en la solicitud de asignaturas", Toast.LENGTH_SHORT).show()
                        }
                    })

                } else {
                    // Manejar el caso de respuesta fallida
                    Toast.makeText(this@HomeActivity, "Error al obtener las calificaciones", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Calificaciones>>, t: Throwable) {
                // Manejar el caso de fallo en la solicitud
                Toast.makeText(this@HomeActivity, "Error en la solicitud", Toast.LENGTH_SHORT).show()
            }
        })

    }



    private fun setup(email: String, provider: String) {
        /*//Borrado de datos
        val prefs : SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE).edit()
        prefs?.clear()
        prefs?.apply()

        title = "Inicio"
        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }*/
        val prefs: SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        title = "Inicio"
        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // Borrar los datos al cerrar sesión
            prefs.edit().clear().apply()
            onBackPressed()
        }

    }

}