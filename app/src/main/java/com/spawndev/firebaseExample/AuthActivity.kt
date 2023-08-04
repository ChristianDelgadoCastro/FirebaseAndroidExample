package com.spawndev.firebaseExample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.spawndev.firebaseExample.model.Calificaciones
import com.spawndev.firebaseExample.network.ApiService
import com.spawndev.firebaseExample.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class AuthActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    private lateinit var signUpButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var logInButton: Button
    private lateinit var googleButton: Button
    private lateinit var authLayout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        //Splash
        Thread.sleep(2000) //Esperar en el logotipo para que se aprecie
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        // Ocultar la barra de título
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_auth)

        // Referencias de las vistas
        signUpButton = findViewById(R.id.signUpButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        logInButton = findViewById(R.id.logInButton)
        googleButton = findViewById(R.id.googleButton)
        authLayout = findViewById(R.id.authLayout)

        // Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "integracion de Firebase complta")
        analytics.logEvent("InitScreen",bundle)

        // Remote Config
        val configSettings : FirebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        val firebaseConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        firebaseConfig.setConfigSettingsAsync(configSettings)
        firebaseConfig.setDefaultsAsync(mapOf("show_error_button" to false, "error_button_text" to "Error"))

        signUpButton.visibility = View.INVISIBLE
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val showRegisterButtonRemote = Firebase.remoteConfig.getBoolean("Register_button_show")
                val RegisterButtonTextRemote = Firebase.remoteConfig.getString("Register_button_text")

                if (showRegisterButtonRemote) {
                    signUpButton.visibility = View.VISIBLE
                }else {
                    signUpButton.text = RegisterButtonTextRemote
                }
            }
        }

        // Setup
        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        authLayout.visibility = View.VISIBLE
        emailEditText.setText("")
        passwordEditText.setText("")
    }

    private fun session() {
        val prefs : SharedPreferences = getSharedPreferences(getString(R.string.prefs_file),
            Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email != null && provider != null){
            authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Autenticacion"
        signUpButton.setOnClickListener{
            signUpButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (isICIIBAEmail(email)) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showHome(task.result?.user?.email ?: "", ProviderType.BASIC)
                                } else {
                                    showAlert()
                                }
                            }
                    } else {
                        showAlertCorreo()
                    }
                }
            }
            googleButton.setOnClickListener {
                //Configuracion
                val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleClient = GoogleSignIn.getClient(this, googleConf)
                googleClient.signOut()
                startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
            }
        }

        logInButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailEditText.text.toString(),
                        passwordEditText.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }
            }
        }

    }

    private fun isICIIBAEmail(email: String): Boolean {
        return email.endsWith("@iciiba.edu.mx", ignoreCase = true)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertCorreo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Correo no válido")
        builder.setMessage("Debes pertenecer a la organizacion ICIIBA para entrar")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.obtenerCalificacionesPorEmail(email)

        call.enqueue(object : Callback<List<Calificaciones>> {
            override fun onResponse(call: Call<List<Calificaciones>>, response: Response<List<Calificaciones>>) {
                if (response.isSuccessful) {
                    val calificacionesList = response.body()
                    if (calificacionesList != null) {
                        val calificacionesArrayList = ArrayList<Calificaciones>(calificacionesList)
                        // Aquí tienes la lista de calificaciones del usuario
                        // Puedes mostrarlas en la HomeActivity o hacer lo que desees con ellas
                        // Por ejemplo, puedes pasarlas como parámetro a la HomeActivity y mostrarlas en una lista
                        val homeIntent = Intent(this@AuthActivity, HomeActivity::class.java).apply {
                            putExtra("email", email)
                            putExtra("provider", provider.name)
                            putParcelableArrayListExtra("calificaciones", calificacionesArrayList)
                        }
                        startActivity(homeIntent)
                    }
                } else {
                    // Manejar el caso de respuesta fallida
                    Toast.makeText(this@AuthActivity, "Error al obtener las calificaciones", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Calificaciones>>, t: Throwable) {
                // Manejar el caso de fallo en la solicitud
                Log.e("RetrofitError", "Error en la solicitud: ${t.message}")
                Toast.makeText(this@AuthActivity, "Error en la solicitud", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val email = account.email ?: ""

                    if (isICIIBAEmail(email)) {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                        FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    showHome(email, ProviderType.GOOGLE)
                                } else {
                                    showAlert()
                                }
                            }
                    } else {
                        showAlertCorreo()
                    }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }

}