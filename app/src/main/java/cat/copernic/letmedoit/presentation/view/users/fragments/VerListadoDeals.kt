package cat.copernic.letmedoit.presentation.view.users.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.Utils.datahepers.DealsUsersServicesJoin
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.Utils.datahepers.HistoryDeal
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.presentation.adapter.users.DealsAdapter

import cat.copernic.letmedoit.databinding.FragmentVerListadoDealsBinding
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.DealViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment que infla y gestiona la pantalla de tratos de un usuario
 * Muestra una lista con todos los tratos del usuario logeado
 * Utiliza ViewModel para comunicar vista repositorio
 */
@AndroidEntryPoint
class verListadoDeals : Fragment() {

    private var _binding: FragmentVerListadoDealsBinding? = null
    private val binding get() = _binding!!
    lateinit var dealsRecyclerView: RecyclerView
    private lateinit var adapter: DealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //ViewModels necesarios para la comunicacion con el repositorio
    private val userViewModel : UserViewModel by viewModels()
    private val dealViewModel : DealViewModel by viewModels()
    private val serviceViewModel : ServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        historyDealsIndex = 0
        historyDeals.clear()
        userDeals.clear()
        users.clear()
        services.clear()
        _binding = FragmentVerListadoDealsBinding.inflate(inflater, container, false)
        userViewModel.getHistoryDeals()
        //Iniciar observers que monitorizan el proceso de las operacions con la base de datos
        initObservers()
        return binding.root
    }

    /**
     * Inicia la configuracion de la vista.
     * Principalmente incia el recycler view
     */
    private fun initView() {
        initRecyclerView()
    }

    /**
     * Oculta la animacion de carga
     */
    private fun hideProgress(){
        binding.loadingDeals.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress(){
        binding.loadingDeals.isVisible = true
    }

    private var historyDeals = ArrayList<HistoryDeal>()
    private var userDeals = ArrayList<Deal>()
    private var users = ArrayList<Users>()
    private var services = ArrayList<Service>()

    private var historyDealsIndex = 0
    private var tempDeals = ArrayList<Deal>()

    /**
     * Funcion que inicializa los obervsers
     * Monitoriza:
     *  Lectura de tratos
     *  Lectura de usuarios
     *  Lectura de servicios
     */
    private fun initObservers() {
        userViewModel.getHistoryDealsState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<ArrayList<HistoryDeal>> -> {
                    historyDeals.addAll(dataState.data)
                    historyDeals.forEach { historyDeal ->
                        if (historyDeal.dealId.size == 0) hideProgress()
                        historyDeal.dealId.forEach {
                            dealViewModel.getDeal(it.deal_id)
                        }
                    }
                    if(historyDeals.size == 0 ) hideProgress()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> { showProgress() }
                else -> Unit
            }
        } )
        dealViewModel.getDealState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Deal> -> {
                    tempDeals.add(dataState.data)

                    if(historyDeals[historyDealsIndex].dealId.size == tempDeals.size){
                        tempDeals.forEach {
                            userDeals.add(it)
                            if(it.users.userOneId == Constants.USER_LOGGED_IN_ID) {
                                userViewModel.getUser(it.users.userTwoId)
                            }
                            else{
                                userViewModel.getUser(it.users.userOneId)
                            }
                        }
                        tempDeals.clear()
                        historyDealsIndex++
                    }

                    if(historyDealsIndex >= historyDeals.size) return@Observer
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Users?> -> {
                    dataState.data?.let { users.add(it) }
                    if(users.size == userDeals.size) userDeals.forEach {
                        if(it.users.userOneId == Constants.USER_LOGGED_IN_ID) serviceViewModel.getService(it.services.serviceTwoId)
                        else serviceViewModel.getService(it.services.serviceOneId)
                    }
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Service> -> {
                    services.add(dataState.data)
                    if(services.size == userDeals.size) initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
    }

    /**
     * Funcion que incializa el recycler view
     */
    private fun initRecyclerView() {
        hideProgress()
        dealsRecyclerView = binding.recyclerViewListadoDeals
        binding.recyclerViewListadoDeals.layoutManager = LinearLayoutManager(binding.root.context)


        val test = userDeals
        val test2 = users
        val test3 = services
        val dealsItemToShow = ArrayList<DealsUsersServicesJoin>()
        userDeals.forEachIndexed {i, userDeal ->
            val usersFiltered = users.filter { it.id == if(userDeal.users.userOneId == Constants.USER_LOGGED_IN_ID) userDeal.users.userTwoId else userDeal.users.userOneId }
            val servicesFiltered = services.filter { it.id == if(userDeal.users.userOneId == Constants.USER_LOGGED_IN_ID) userDeal.services.serviceTwoId else userDeal.services.serviceOneId }

            if(usersFiltered.isNullOrEmpty())return
            if(servicesFiltered.isNullOrEmpty())return
            val user = usersFiltered[0]
            val service = servicesFiltered[0]
            dealsItemToShow.add(DealsUsersServicesJoin(
                userDeal,
                Constants.USER_LOGGED_IN,
                user,
                service
            ))
        }

        adapter = DealsAdapter(dealsItemToShow)

        dealsRecyclerView.adapter = adapter
    }

}