package cat.copernic.letmedoit.presentation.view.users.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Opinion
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.databinding.FragmentRateUserBinding
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que infla y gestiona la pantalla para opinar sobre un usuario
 * Utiliza el ViewModel para comunicarse con el repositorio (bd)
 */
@AndroidEntryPoint
class RateUser : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val user by lazy {
        args.user
    }
    private val userViewModel : UserViewModel by viewModels()
    private val args : RateUserArgs by navArgs()
    lateinit var binding : FragmentRateUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRateUserBinding.inflate(inflater,container,false)
        initView()
        initObservers()
        initListeners()
        return binding.root
    }

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.saveBtn.setOnClickListener{ addOpinion() }
        binding.btnBack.setOnClickListener{ requireActivity().onBackPressed()}
    }

    /**
     * Publica la opinion del usuario
     */
    private fun addOpinion() {
        val opinion = Opinion(
            userId = Constants.USER_LOGGED_IN_ID,
            rating = binding.ratingBar.rating,
            description = binding.editOpinionInput.text.toString(),
            deal_id = args.dealId,
            serviceId = args.serviceId
        )
        userViewModel.addOpinion(opinion,user.id)
    }

    /**
     * Inicia los observers que monitorizan el proceso de las operacions con la base de datos
     */
    private fun initObservers() {
        userViewModel.addOpinionState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    hideProgress()

                    val action = RateUserDirections.rateUserToHome()
                    Navigation.findNavController(requireView()).navigate(action)
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                    hideProgress()
                }
                is DataState.Loading -> { showProgress() }
                else -> Unit
            }
        } )
    }

    /**
     * Oculta la animacion de carga
     */
    private fun hideProgress(){
        binding.saveLoading.isVisible = false
        binding.saveBtn.isVisible = true
        binding.saveBtn.isEnabled = true
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress(){
        binding.saveBtn.isVisible = false
        binding.saveBtn.isEnabled = false
        binding.saveLoading.isVisible = true
    }

    /**
     * Inicia la vista
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        if(user.avatar != "") Picasso.get().load(user.avatar).into(binding.iconUser)
        binding.user.text = "${user.name} ${user.surname} \n @${user.username}"
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RateUser().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}