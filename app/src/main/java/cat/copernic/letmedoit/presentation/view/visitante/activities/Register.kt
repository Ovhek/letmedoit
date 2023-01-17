package cat.copernic.letmedoit.presentation.view.visitante.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.presentation.viewmodel.visitante.RegisterViewModel
import cat.copernic.letmedoit.databinding.ActivityRegisterBinding
import cat.copernic.letmedoit.presentation.view.general.activities.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern


/**
 * Activity que infla y gestiona la pantalla de registro
 */

//Marca que la clase a fin de inyectarla.
@AndroidEntryPoint
class Register : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    // Variable que contiene el auth del usuario registrado
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inicia los observers encargados de observar el estado de las operaciones
        //con la base de datos
        initObservers()

        auth = Firebase.auth

        //Listeners encargados de registrarse y retroceder
        binding.btnExitRegister.setOnClickListener { onBackPressed() }
        binding.txtSignIn.setOnClickListener { onBackPressed() }
        binding.btnSignIn.setOnClickListener() { checkLogin() }

    }

    // ViewModel que comunica la activity con el repositorio
    private val registerViewModel: RegisterViewModel by viewModels()

    /**
     * Funcion que inicia los observers
     */
    private fun initObservers() {

        //Monitoria el estado de registro de un usuario
        registerViewModel.registerState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users> -> {
                    registerViewModel.saveUser(dataState.data)
                }
                is DataState.Error -> {
                    hideProgress()
                    Utils.showOkDialog(resources.getString(R.string.error), this, dataState.exception.message.toString(),this)
                }
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })

        //Monitoriza el estado del proceso de guardar un usuario en la base de datos
        registerViewModel.saveUserState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    startActivity(Intent(this, Home::class.java))
                    finish()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}", this, dataState.exception.message.toString(),this)
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
        binding.registerLoading.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        binding.btnSignIn.isEnabled = false
        binding.btnSignIn.text = ""
        binding.registerLoading.isVisible = true
    }

    /**
     * Comprueva que los campos requidos para registrarse sean correctos
     * Si son incorrectos muestra un Alert Dialog que avisa al usuario
     * Si son correctos registra al usuario e inicia sesion
     */
    private fun checkLogin() {
        var email = binding.editEmail.text.toString().trim().lowercase()
        var password = binding.editPassword.text.toString()
        var confirmPassword = binding.editConfirmPassword.text.toString()
        val username = binding.editUsername.text.toString().trim()

        if(username.isEmpty()){
            Utils.showOkDialog(resources.getString(R.string.error),this,resources.getString(R.string.usernameEmptyErro), this)
            return
        }
        if(email.isEmpty()){
            Utils.showOkDialog(resources.getString(R.string.error),this,resources.getString(R.string.emailemptyerror), this)
            return
        }
        if (password.isEmpty() or confirmPassword.isEmpty()) {
            Utils.showOkDialog(resources.getString(R.string.error),this,resources.getString(R.string.emailorPasswordEmpty), this)
            return
        }
        if (password != confirmPassword) {
            Utils.showOkDialog(resources.getString(R.string.error),this,resources.getString(R.string.passwordNotMatch), this)
            return
        }

        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        if (!pattern.matcher(email).matches()) {
            Utils.showOkDialog(resources.getString(R.string.error),this,resources.getString(R.string.notvalidemail), this)
            return
        }

        registerUser(email,password,username)

    }

    /**
     * Registra un usuario en la base de datos
     * @param email correo electronico del usuario
     * @param password contraseña del usuario
     * @param username nombre de usuario
     */
    private fun registerUser(email: String, password: String, username: String) {
        registerViewModel.register(createUser(email, username), password)
    }

    /**
     * Crea una instancia de Users
     * @return Users
     * @param email correo electronico
     * @param username nombre de usuario
     */
    private fun createUser(email: String, username: String): Users {
        return Users(
            email = email,
            rating = 0f,
            username = username
        )
    }

}