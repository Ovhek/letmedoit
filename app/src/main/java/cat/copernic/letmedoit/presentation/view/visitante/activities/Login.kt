package cat.copernic.letmedoit.presentation.view.visitante.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.presentation.view.users.activities.RecoveryPassword_email
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.presentation.viewmodel.visitante.LoginViewModel
import cat.copernic.letmedoit.databinding.ActivityLoginBinding
import cat.copernic.letmedoit.presentation.view.general.activities.Home
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

//Marca que la clase a fin de inyectarla.
/**
 * Activity que infla y gestiona la pantalla de login
 */
@AndroidEntryPoint
class Login : AppCompatActivity() {


    // Variable que contiene el auth del usuario iniciado
    private lateinit var auth: FirebaseAuth

    // ViewModel que conecta el repositorio con el fragment
    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Iniciamos los obervers que se encargan de obervar
         * el estado de las operacions con la base de datos
         */
        initObservers()

        auth = Firebase.auth

        /**
         * Listeners engargados de login, register, password recovery y retroceder
         */
        binding.btnSignIn.setOnClickListener { checkLogin() }
        binding.btnSignUp.setOnClickListener { signUp() }
        binding.btnExitLogin.setOnClickListener { onBackPressed() }
        binding.btnForgotPassword.setOnClickListener { forgotPassword() }


    }

    /**
     * Funcion que se encargar de iniciar los observers
     */
    private fun initObservers() {
        //Monitoriza el estado del login de un usuario
        loginViewModel.loginState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    loginViewModel.getUserData()
                }
                is DataState.Error -> {
                    hideProgress()
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        this,
                        dataState.exception.message.toString(),
                        this
                    )
                }
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })
        //Monitoriza el estado de lectura del usuario
        loginViewModel.userDataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    startActivity(Intent(this, Home::class.java))
                    finish()

                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        this,
                        dataState.exception.message.toString(),
                        this
                    )
                }
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })
    }

    /**
     * Esconde la animacion de carga
     */
    private fun hideProgress() {
        binding.btnSignIn.isEnabled = true
        binding.btnSignIn.text = resources.getString(R.string.txt_login_signIn)
        binding.loginLoading.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        binding.btnSignIn.isEnabled = false
        binding.btnSignIn.text = ""
        binding.loginLoading.isVisible = true
    }

    /**
     * Nos lleva a la activity para recuperar la contraseña
     */
    private fun forgotPassword() {
        startActivity(
            Intent(
                this,
                RecoveryPassword_email::class.java
            )
        )
    }

    /**
     * Nos lleva a la activity para registrar un usuario
     */
    private fun signUp() {
        startActivity(Intent(this, Register::class.java))
    }

    /**
     * Comprueva que los campos necesarios para iniciar sesion sean correctos
     * Si los campos son incorrectos muestra un Alert Dialog
     * Si los campos son correctos inicia sesion
     */
    private fun checkLogin() {
        var email = binding.editUsername.text.toString().trim().lowercase()
        var password = binding.editPassword.text.toString()

        if (email.isEmpty() or password.isEmpty()) {
            Utils.showOkDialog(
                resources.getString(R.string.error),
                this,
                resources.getString(R.string.emailemptyerror),
                this
            )
            return
        }

        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        if (!pattern.matcher(email).matches()) {
            Utils.showOkDialog(
                resources.getString(R.string.error),
                this,
                resources.getString(R.string.notvalidemail),
                this
            )
            return
        }

        loginUser(email, password)
    }

    /**
     * Funcion que inicia sesion
     * @param email correo electronico del usuario
     * @param password contraseña del usuario
     */
    private fun loginUser(email: String, password: String) {
        loginViewModel.login(email, password)
    }

}

