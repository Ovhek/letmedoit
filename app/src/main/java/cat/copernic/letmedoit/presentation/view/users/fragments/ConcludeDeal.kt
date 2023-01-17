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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.data.model.Opinion
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.databinding.FragmentConcludeDealBinding
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.DealViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment que infla y gestiona la pantalla para concluir un trato
 * Utiliza el ViewModel para comunicarse con los repositorios corresponidentes
 */
@AndroidEntryPoint
class concludeDeal : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private val args : concludeDealArgs by navArgs()
    private val user by lazy {
        args.user
    }
    private val deal by lazy {
        args.deal
    }

    private val dealViewModel : DealViewModel by viewModels()
    private val serviceViewModel : ServiceViewModel by viewModels()
    private val userViewModel : UserViewModel by viewModels()

    private var services = ArrayList<Service>()
    lateinit var binding: FragmentConcludeDealBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConcludeDealBinding.inflate(inflater,container,false)
        binding.concludeBtn.isVisible = false
        services.clear()
        serviceViewModel.getService(deal.services.serviceOneId)
        initListeners()
        initObservers()
        return binding.root
    }

    private var goToOpinions = true
    private lateinit var myService: Service
    private lateinit var  hisService: Service

    /**
     * Inicia la vista
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.concludeBtn.isVisible = true
        if(deal.users.userOneId == Constants.USER_LOGGED_IN_ID){
            myService = services[0]
            hisService = services[1]
        }
        else{
            myService = services[1]
            hisService = services[0]
        }
        if(user.avatar != "") Picasso.get().load(user.avatar).into(binding.iconUser)
        manageDealProgress()

        binding.nameSurname.text = "${user.name} ${user.surname} \n @${user.username}"
        binding.myServiceSubText.text = myService.title
        binding.hisServiceSubText.text = hisService.title
        binding.descriptionEdit.setText(deal.description)
        dealViewModel.suscribeForUpdates(deal.id)
    }

    /**
     * Configura la vista segun los datos del trato
     */
    private fun manageDealProgress(){
        binding.dealProgress.text =
            when(deal.conclude){
                0 -> "0/2"
                1 -> "1/2"
                2 -> "1/2"
                else -> "2/2"
            }
        binding.concludeBtn.isEnabled = !(Constants.USER_LOGGED_IN_ID == deal.users.userOneId && deal.conclude == 1
                || Constants.USER_LOGGED_IN_ID == deal.users.userTwoId && deal.conclude == 2
                || deal.conclude == 3)
    }

    /**
     * Lleva al usuario a la pantalla para dar una opinion tras finalizar el trato
     */
    private fun goToAddOpinion(){
        val action  = concludeDealDirections.concludeDealToRateUser(user,hisService.id,deal.id)
        requireView().findNavController().navigate(action)
    }

    /**
     * Inicia los obsevers que monitorizan el proceso de las operaciones con la base de datos
     */
    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Service> -> {
                    services.add(dataState.data)
                    if(services.size == 1) serviceViewModel.getService(deal.services.serviceTwoId)

                    if(services.size == 2) initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        dealViewModel.suscribeForUpdatesState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Deal?> -> {
                    deal.conclude = dataState.data?.conclude ?: deal.conclude
                    manageDealProgress()
                    if(deal.conclude == 3) userViewModel.getOpinions(user.id)
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        userViewModel.getOpinionsState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<ArrayList<Opinion>> -> {
                    if(dataState.data.size > 0)
                        dataState.data.forEach {
                            if(deal.id == it.deal_id) goToOpinions = false
                        }
                    if (goToOpinions) goToAddOpinion()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        dealViewModel.denyState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    userViewModel.deleteDealFromHistory(deal.id,Constants.USER_LOGGED_IN_ID,user.id)
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        userViewModel.deleteDealFromHistoryState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    if (firstDelete) {
                        userViewModel.deleteDealFromHistory(deal.id,user.id,Constants.USER_LOGGED_IN_ID)
                        firstDelete = false
                    }
                    else requireActivity().onBackPressed()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
    }

    var firstDelete = true

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.seeMyService.setOnClickListener { goToViewService(myService) }
        binding.seeHisService.setOnClickListener { goToViewService(hisService) }
        binding.concludeBtn.setOnClickListener { dealConclude() }
        binding.denyConclude.setOnClickListener { cancelDeny() }
    }

    /**
     * Rechaza un trato
     */
    private fun cancelDeny() {
        dealViewModel.deny(deal.id)
    }

    /**
     * Conluye un trato
     */
    private fun dealConclude() {
        dealViewModel.conclude(deal.id)
    }

    /**
     * Lleva al usuario a la pantalla para ver el servicio del otro usuario
     */
    private fun goToViewService(service: Service) {
        val action  = concludeDealDirections.actionConcludeDealToViewService(service)
        requireView().findNavController().navigate(action)
    }
}