package cat.copernic.letmedoit.presentation.view.general.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.*
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.ActivityHomeBinding
import cat.copernic.letmedoit.presentation.view.admin.activities.MenuAdmin
import cat.copernic.letmedoit.di.FirebaseModule
import cat.copernic.letmedoit.presentation.view.general.fragments.Menu_Inferior
import cat.copernic.letmedoit.presentation.view.users.fragments.CreateDealDirections
import cat.copernic.letmedoit.presentation.view.visitante.activities.UserBanned
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Activity que carga todos los fragments del home y los gestiona
 * Tambien se encarga de comprovar el tipo de usuario loegado y relizar las
 * operacions correpondientes.
 */
@AndroidEntryPoint
class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val userViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.loadingHome.isVisible = true
        (supportFragmentManager.fragments[1] as Menu_Inferior).binding.menuInferior.isVisible =
            false
        askPermissions()

        val currentUser = FirebaseModule.firebaseAuthProvider().currentUser
        if (currentUser != null) {
            Constants.USER_LOGGED_IN_ID = currentUser.uid
            initObserver()
            userViewModel.getUser(Constants.USER_LOGGED_IN_ID)
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        this,
                        "Fetching FCM registration token failed ${task.exception}",
                        this
                    )
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Constants.TOKEN = token
                userViewModel.addDeviceToken(token)
            })
        } else {
            binding.navController.getFragment<Fragment>().findNavController()
                .setGraph(R.navigation.app_navigation_visitante)
        }

    }

    /**
     * Pregunta pos los permisos necesarios para la aplicacion
     */
    private fun askPermissions() {
        val permissions = ArrayList(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions.toTypedArray(), 101)
        }
    }

    /**
     * Inicializa los obervers
     */
    private fun initObserver() {
        userViewModel.getUserState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users?> -> {
                    val user = dataState.data
                    if (user != null) {
                        Constants.USER_LOGGED_IN = user
                    }
                    manageUserNavigation(user)
                }
                is DataState.Error -> {
                    //Utils.showOkDialog("${resources.getString(R.string.error)}",this,dataState.exception.message.toString())
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
    }

    /**
     * Dependiendo de si el usuario es admin, visitante o usuario, se utiliza un NavController u Otro.
     */
    private fun manageUserNavigation(currentUser: Users?) {

        if (currentUser == null) {
            binding.navController.getFragment<Fragment>().findNavController()
                .setGraph(R.navigation.app_navigation_visitante)
            return
        }

        //Usuario baneado
        if (currentUser.banned) {
            startActivity(Intent(this, UserBanned::class.java))
            finish()
        }
        //Usuario admin
        if (currentUser.admin) {
            startActivity(Intent(this, MenuAdmin::class.java))
            finish()
        }
        //Usuario logeado
        else {
            binding.navController.getFragment<Fragment>().findNavController()
                .setGraph(R.navigation.app_navigation_leogeado)
        }
        (supportFragmentManager.fragments[1] as Menu_Inferior).binding.menuInferior.isVisible = true
        //binding.loadingHome.isVisible = false

        showReviewReminder()
    }

    /**
     * Función encargada de revisar los dias que se ha ejecutado la aplicación y mostrar una opción para añadir una reseña a Google Play al cabo de dos disas.
     * Código obtenido de https://developer.android.com/guide/playcore/in-app-review/kotlin-java
     */
    private fun showReviewReminder() {
        val prefs = getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val launchCount = prefs.getInt("launchCount", 0) + 1
        editor.putInt("launchCount", launchCount)
        editor.apply()

        if (launchCount == 2) {
            val manager = ReviewManagerFactory.create(this)
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                } else {
                    // There was some problem, log or handle the error code.

                }
            }
        }
    }

    /**
     * Control para volver hacia atras en los recyclerviews, si el destino actual es la primera pantalla no vuelve hacia atras
     */
    override fun onBackPressed() {
        val currentDestination = binding.navController.findNavController().currentDestination
        val sourceDestination =
            binding.navController.findNavController().previousBackStackEntry?.destination
        if (currentDestination != null) {
            if (sourceDestination != null) {
                if (sourceDestination.label == "fragment_home") {
                    if (currentDestination.label == "fragment_ver_conversaciones") return
                    if (currentDestination.label == "fragment_profiles_services_manager_vis") return
                    if (currentDestination.label == "fragment_new_service") return
                    if (currentDestination.label == "fragment_opciones_de_cuenta") return
                }
                if (currentDestination.label == "fragment_conclude_deal" && sourceDestination.label == "fragment_ver_conversaciones") findNavController(
                    R.id.navController
                ).navigate(R.id.verConversaciones)
                if (currentDestination.label == "fragment_conclude_deal" && sourceDestination.label == "fragment_ver_deal") findNavController(
                    R.id.navController
                ).navigate(R.id.verConversaciones)
                if (currentDestination.label == "fragment_ver_conversaciones" && sourceDestination.label == "fragment_ver_deal") return
                if (currentDestination.label == "fragment_ver_conversaciones" && sourceDestination.label == "fragment_conclude_deal") return
            }
            if (currentDestination.label == "fragment_rate_user") findNavController(R.id.navController).navigate(
                R.id.verConversaciones
            )
            if (currentDestination.label == "fragment_conclude_deal") findNavController(R.id.navController).navigate(
                R.id.verConversaciones
            )
            if (currentDestination.label == "fragment_rate_user") findNavController(R.id.navController).navigate(
                R.id.verConversaciones
            )
            if (currentDestination.label == "fragment_ver_deal") findNavController(R.id.navController).navigate(
                R.id.verConversaciones
            )
            else if ((currentDestination.id
                    ?: -1) != R.id.homeFragment
            ) findNavController(R.id.navController).popBackStack()
        }

    }
}