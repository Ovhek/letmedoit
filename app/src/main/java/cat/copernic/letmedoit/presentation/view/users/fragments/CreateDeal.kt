package cat.copernic.letmedoit.presentation.view.users.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.Utils.datahepers.ServicesMap
import cat.copernic.letmedoit.Utils.datahepers.UserServices
import cat.copernic.letmedoit.Utils.datahepers.UsersMap
import cat.copernic.letmedoit.data.model.*
import cat.copernic.letmedoit.databinding.FragmentCreateDealBinding
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.DealViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment que infla y gestiona la pantalla para crear un trato
 * Utiliza ViewModel para comuncarse con los respectivos repositorios
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CreateDeal : Fragment() {

    private lateinit var binding: FragmentCreateDealBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateDealBinding.inflate(inflater, container, false)
        initView()
        initObservers()
        initListeners()
        return binding.root
    }

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.addButton.setOnClickListener { goToServiceManager("null") }
        binding.editButton.setOnClickListener { goToServiceManager(servicesUserOne[binding.spinnerMyService.selectedItemPosition].id) }
        binding.btnCreate.setOnClickListener { createDeal() }
        binding.backArrow.setOnClickListener { requireActivity().onBackPressed() }
    }

    private lateinit var deal: Deal
    private val dealViewModel: DealViewModel by viewModels()

    /**
     * Crea un trato y lo guarda en la base de datos
     */
    private fun createDeal() {
        showProgress()
        deal = Deal(
            users = UsersMap(args.userOne.id, args.userTwo.id),
            services = ServicesMap(
                servicesUserOne[binding.spinnerMyService.selectedItemPosition].id,
                args.serviceToDealWith.id
            ),
            description = binding.textAreaDescription.text.toString(),
            conclude = 0,
            accepted = false
        )
        dealViewModel.insert(deal)
    }

    /**
     * Funcion que lleva al fragment para editar el servicio
     * @param idService servicio
     */
    private fun goToServiceManager(idService: String) {
        val action = CreateDealDirections.actionCreateDealToNewService(idService)
        Navigation.findNavController(requireView()).navigate(action)
    }


    private var gettingUserOneServices = true

    private var servicesUserOneIds = ArrayList<UserServices>()
    private var servicesUserTwoIds = ArrayList<UserServices>()

    private var servicesUserOne = ArrayList<Service>()
    private var servicesUserTwo = ArrayList<Service>()

    private var addedHistoryDeals = 0

    /**
     * Inicia los observers que monitorizan el proceso de las operaciones con la base de datos
     */
    private fun initObservers() {
        userViewModel.getServicesState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ArrayList<UserServices>> -> {

                    if (gettingUserOneServices) {
                        servicesUserOneIds.addAll(dataState.data)
                        if (servicesUserOneIds.size == 0) {
                            gettingUserOneServices = false
                            args.userTwo.id.let { userViewModel.getServices(it) }
                        }
                        servicesUserOneIds.forEach { serviceViewModel.getService(it.service_id) }
                    } else {
                        servicesUserTwoIds.addAll(dataState.data)
                        dataState.data.forEach { serviceViewModel.getService(it.service_id) }
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
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    if (gettingUserOneServices) servicesUserOne.add(dataState.data)
                    else servicesUserTwo.add(dataState.data)

                    if (servicesUserOne.size == servicesUserOneIds.size && gettingUserOneServices) {
                        initSpinner(servicesUserOne, binding.spinnerMyService)
                        gettingUserOneServices = false
                        args.userTwo.id.let { userViewModel.getServices(it) }
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
        dealViewModel.insertState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    userViewModel.addHistoryDeal(args.userOne.id, args.userTwo.id, deal.id)
                    userViewModel.addHistoryDeal(args.userTwo.id, args.userOne.id, deal.id)
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    hideProgress()
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        userViewModel.addHistoryDealState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    addedHistoryDeals++
                    if (addedHistoryDeals == 2) {
                        hideProgress()
                        val action = CreateDealDirections.actionCreateDealToHomeFragment(-1, null)
                        Navigation.findNavController(requireView()).navigate(action)
                    }
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    hideProgress()
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
    }

    /**
     * Oculta la animacion de carga
     */
    private fun hideProgress() {
        binding.btnCreate.isEnabled = true
        binding.btnCreate.isVisible = true
        binding.createDealLoading.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        binding.btnCreate.isEnabled = false
        binding.btnCreate.isVisible = false
        binding.createDealLoading.isVisible = true

    }

    /**
     * Inicia el spinner para elegir un servicio (Usuraio actual)
     */
    private fun initSpinner(services: ArrayList<Service>, spinner: Spinner) {
        Utils.AsignarPopUpSpinner(requireContext(), ArrayList(services.map { it.title }), spinner)
        hideProgress()
    }


    private val serviceViewModel: ServiceViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private val args: CreateDealArgs by navArgs()

    /**
     * Inicia la vista
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.btnCreate.isEnabled = false
        args.userTwo.let { user ->
            if (user.avatar != "") Picasso.get().load(user.avatar).into(binding.userImage)
            binding.nameSurnameText.text = "${user.name} ${user.surname} \n @${user.username}"
        }
        args.userOne.id?.let { userViewModel.getServices(it) }
        binding.txtHisService.setText(args.serviceToDealWith.title)
    }

    override fun onResume() {
        super.onResume()
        gettingUserOneServices = true

        servicesUserOneIds = ArrayList()
        servicesUserTwoIds = ArrayList()

        servicesUserOne = ArrayList()
        servicesUserTwo = ArrayList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateDeal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateDeal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}