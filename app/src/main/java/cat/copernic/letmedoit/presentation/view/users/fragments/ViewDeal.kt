package cat.copernic.letmedoit.presentation.view.users.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentVerDealBinding
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.DealViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

/**
 * Fragment que infla y gestiona la vista de un trato del usuario.
 * Muestra un el trato al usuario, permitiendo aceptar, rechazar y concluir.
 * Utiliza un ViewModel para comunicarse con el repositorio respectivo.
 */
@AndroidEntryPoint
class ViewDeal : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    // ViewModel que comunica el fragment con el repositorio de usuarios
    private val userViewModel: UserViewModel by viewModels()

    // ViewModel que comunica el fragment con el repositorio de servicios
    private val serviceViewModel: ServiceViewModel by viewModels()

    //Argumentos que contiene la id del trato
    private val args: ViewDealArgs by navArgs()

    // ViewModel que comunica el fragment con el repositorio de tratos
    private val dealViewModel: DealViewModel by viewModels()

    lateinit var binding: FragmentVerDealBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerDealBinding.inflate(inflater, container, false)

        //Comprueba si el trao esta concluido, en ese caso nos dirige al fragment
        //correspondiente
        if (deal.accepted) goToConcludeDeal()

        //Obtenemos la id del usuario
        userViewModel.getUser(Constants.USER_LOGGED_IN_ID)

        //Manejamos el estado del trato a tiempo real
        dealViewModel.suscribeForUpdates(deal.id)

        services.clear()
        //Obtenemos el servicio del trato
        serviceViewModel.getService(deal.services.serviceOneId)

        //Iniciamos los controladores
        initListeners()

        //Iniciamos obervers que observan el estado de las operacions con la base de datos
        initObservers()
        return binding.root
    }

    private lateinit var myUser: Users
    private val user by lazy {
        args.user
    }
    private val deal by lazy {
        args.deal
    }
    private val services = ArrayList<Service>()

    /**
     * Funcion que inicia los obervers
     */
    private fun initObservers() {
        //Monitoriza el estado de lectura de un usuario de la base de datos
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users?> -> {
                    if (dataState.data != null)
                        myUser = dataState.data
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        //Monitoriza el estao de lectura de un servicio de la base de datos
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    services.add(dataState.data)
                    if (services.size == 1) serviceViewModel.getService(deal.services.serviceTwoId)

                    if (services.size == 2) initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        //Monitoriza el estado a tiempo real de un trato
        dealViewModel.suscribeForUpdatesState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Deal?> -> {
                    val deal = dataState.data
                    if (deal != null) {
                        if (deal.accepted) goToConcludeDeal()
                    }
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        //Monitoriza el cambio de estado de un trato a aceptado en la base de datos
        dealViewModel.acceptState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    deal.accepted = true

                    if (deal.accepted) goToConcludeDeal()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        //Monitoriza el cambio de estado de un trato a rechazado en la base de datos
        dealViewModel.denyState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    userViewModel.deleteDealFromHistory(
                        deal.id,
                        Constants.USER_LOGGED_IN_ID,
                        user.id
                    )
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        //Monitoriza el estado de eliminacion de un trato de la base de datos
        userViewModel.deleteDealFromHistoryState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    if (firstDelete) {
                        userViewModel.deleteDealFromHistory(
                            deal.id,
                            user.id,
                            Constants.USER_LOGGED_IN_ID
                        )
                        firstDelete = false
                    } else requireActivity().onBackPressed()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
    }

    private var firstDelete = true

    /**
     * Funcion que nos lleva al fragment que muestra el trato para concluir
     */
    private fun goToConcludeDeal() {
        val action = ViewDealDirections.verDealToConcludeDeal(deal, user)
        requireView().findNavController().navigate(action)
    }

    private lateinit var myService: Service
    private lateinit var hisService: Service
    private fun initListeners() {
        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.seeMyService.setOnClickListener { goToViewService(myService) }
        binding.seeHisService.setOnClickListener { goToViewService(hisService) }
        binding.btnAccept.setOnClickListener { acceptDeal() }
        binding.btnDeny.setOnClickListener { denyDeal() }
    }

    /**
     * Funcion que acepta un trato
     */
    private fun acceptDeal() {
        dealViewModel.accept(deal.id)
    }

    /**
     * Funcion para rechazar un trato
     */
    private fun denyDeal() {
        dealViewModel.deny(deal.id)
    }

    /**
     * Funcion para ir a ver el servicio
     * @param service Instancia de servicio
     */
    private fun goToViewService(service: Service) {
        val action = ViewDealDirections.actionViewDealToViewService(service)
        requireView().findNavController().navigate(action)
    }

    @SuppressLint("SetTextI18n")
    /**
     * Inicia la configuracion de la vista
     */
    private fun initView() {
        if (user.banned) {
            binding.nameSurname.text = resources.getString(R.string.userbannedmsg)
            binding.btnAccept.isEnabled = false
            return
        }
        if (deal.users.userOneId == Constants.USER_LOGGED_IN_ID) {
            myService = services[0]
            hisService = services[1]
        } else {
            myService = services[1]
            hisService = services[0]
        }

        if (user.avatar != "") Picasso.get().load(user.avatar).into(binding.iconUser)
        binding.txtProgressDeal.text = if (!deal.accepted) "1/2" else "2/2"
        if (Constants.USER_LOGGED_IN_ID == deal.users.userOneId) {
            binding.btnAccept.isEnabled = false
            binding.btnAccept.setBackgroundColor(resources.getColor(R.color.secundario_gris_50))
            binding.btnDeny.text == resources.getString(R.string.cancel)
        } else {
            binding.btnAccept.isEnabled = true
            binding.btnAccept.setBackgroundColor(resources.getColor(R.color.azul_marino))
            binding.btnAccept.setTextColor(resources.getColor(R.color.principal_blanco))
        }
        binding.nameSurname.text = "${user.name} ${user.surname} \n @${user.username}"
        binding.myServiceSubText.text = myService.title
        binding.hisServiceSubText.text = hisService.title
        binding.dealDescription.setText(deal.description)
    }

}