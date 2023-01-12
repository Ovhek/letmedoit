package cat.copernic.letmedoit.presentation.view.users.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.presentation.viewmodel.visitante.LoginViewModel
import cat.copernic.letmedoit.databinding.FragmentOpcionesDeCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.*
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.data.provider.LenguagesProvider
import cat.copernic.letmedoit.presentation.view.general.activities.Home
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso

import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountOptions.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AccountOptions : Fragment() {

    private lateinit var user: Users
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val  userViewModel  : UserViewModel  by viewModels()
    private val  loginViewModel : LoginViewModel by viewModels()

    private lateinit var binding :FragmentOpcionesDeCuentaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpcionesDeCuentaBinding.inflate(inflater,container,false)
        initView()
        return binding.root
    }

    private fun initView() {
        val user =  Constants.USER_LOGGED_IN

        if(user.avatar != "")
            Picasso.get().load(user.avatar).into(binding.imageUser)
        
        binding.nameSurname.text = "${user.name} ${user.surname} \n @${user.username} \n"
        binding.myRatingBar.rating = user.rating
        binding.ratingNum.text = "(${DecimalFormat("#.##").format(user.rating)})"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        initObservers()

        initSpinner()

        initListeners()


        auth = Firebase.auth

    }

    private fun initListeners() {
        val languagesString = ArrayList<String>()
        LenguagesProvider.obtenerLenguages().map { x -> x.lenguage }.toCollection(languagesString)
    }

    private fun initSpinner() {
        binding.btnUserProfile.setOnClickListener{ goToUserProfile()}
        binding.btnEditProfile.setOnClickListener{ goToEditProfile()}
        binding.btnSignOut.setOnClickListener{ loginViewModel.logOut() }
    }

    private fun goToEditProfile() {
        val action = AccountOptionsDirections.opcionesCuentaToEditarPerfil(Constants.USER_LOGGED_IN)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun goToUserProfile() {
        val action = AccountOptionsDirections.accountOptionsToPerfilUser(Constants.USER_LOGGED_IN_ID)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun initObservers() {
        loginViewModel.logOutState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    Constants.USER_LOGGED_IN_ID = ""
                    startActivity(Intent(requireActivity(), Home::class.java))
                    requireActivity().finish()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
    }
}