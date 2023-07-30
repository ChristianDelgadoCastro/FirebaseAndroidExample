package com.spawndev.firebaseExample

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC,
    GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var providerTextView: TextView
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Referencias de las vistas
        emailTextView = findViewById(R.id.emailTextView)
        providerTextView = findViewById(R.id.providerTextView)
        logOutButton = findViewById(R.id.logOutButton)

        // Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?:"")

        // Guardado de datos
        val prefs : SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE).edit()
        prefs?.putString("email", email)
        prefs?.putString("provider", provider)
        prefs?.apply()

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