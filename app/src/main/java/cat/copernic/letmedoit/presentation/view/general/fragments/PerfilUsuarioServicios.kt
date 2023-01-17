package cat.copernic.letmedoit.presentation.view.general.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.UserConstants
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.Utils.datahepers.UserServices
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.databinding.FragmentPerfilUsuarioServiciosBinding
import cat.copernic.letmedoit.presentation.adapter.general.ServiceAdapter
import cat.copernic.letmedoit.presentation.adapter.users.viewholder.DealsViewHolder
import cat.copernic.letmedoit.presentation.viewmodel.general.SearchViewViewModel
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.DealViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que muestra los servicios de un usuario
 * Utiliza el ViewModel para comuncarse con los repositorios
 */
@AndroidEntryPoint
class PerfilUsuarioServicios(private val servicesId: ArrayList<UserServices>?) : Fragment() {
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

    private val serviceViewModel: ServiceViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private var services = ArrayList<Service>()
    private lateinit var serviceRecyclerView: RecyclerView

    lateinit var adapter: ServiceAdapter
    lateinit var binding: FragmentPerfilUsuarioServiciosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        services.clear()
        initObservers()
        servicesId?.forEach { serviceViewModel.getService(it.service_id) }

        // Inflate the layout for this fragment
        binding = FragmentPerfilUsuarioServiciosBinding.inflate(inflater, container, false)
        serviceRecyclerView = binding.recylerViewServices
        serviceRecyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        //Asignación del adaptador al recyclerview.

        serviceRecyclerView.setHasFixedSize(true)

        val model = ViewModelProvider(requireActivity())[SearchViewViewModel::class.java]
        model.message.observe(viewLifecycleOwner, Observer {
            //Por algún motivo hay un bug donde el buscador enviar un mensaje de filtro sin haber cargado siquiera el fragment cuando se cambia entre ver el servicio y el perfil de usuario??
            if (::adapter.isInitialized)
                adapter.filter(it)
        })

        return binding.root
    }

    /**
     * Inicia los observers
     */
    private fun initObservers() {
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    services.add(dataState.data)
                    if (services.size == servicesId!!.size) showServices()
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
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })
        dealsViewModel.getDealsState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Deal>> -> {
                    val test = dealsToDelete
                    dataState.data.forEach {
                        if (it.services.serviceOneId == serviceToDeleteId || it.services.serviceTwoId == serviceToDeleteId) dealsToDelete.add(
                            it
                        )
                    }

                    dealsToDelete.forEach {
                        userViewModel.deleteDealFromHistory(
                            it.id,
                            it.users.userOneId,
                            it.users.userTwoId
                        )
                        userViewModel.deleteDealFromHistory(
                            it.id,
                            it.users.userTwoId,
                            it.users.userOneId
                        )
                        dealsViewModel.deny(it.id)
                    }
                    serviceViewModel.removeService(serviceToDeleteId)
                    userViewModel.deleteService(serviceToDeleteId)

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
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })
        serviceViewModel.removeServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    val service = services.filter { it.id == serviceToDeleteId }[0]
                    services.remove(service)
                    adapter.removeService(service)
                    hideProgress()
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
                is DataState.Loading -> {
                    showProgress()
                }
                else -> Unit
            }
        })
    }

    /**
     * Muestra la animacion de carga a leer los servicios
     */
    private fun showProgress() {
        binding.loadingServices.isVisible = true
    }

    /**
     * Oculta la animacion de carga
     */
    private fun hideProgress() {
        binding.loadingServices.isVisible = false
    }

    /**
     * Muestra los servios del usuario leeidos de la base de datos
     */
    private fun showServices() {
        hideProgress()
        services.forEach {
            if (UserConstants.USER_FAVORITE_SERVICES_IDS.contains(it.id)) it.defaultFav = true
        }
        adapter = ServiceAdapter(services, this, userViewModel, serviceViewModel)
        serviceRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private val dealsViewModel: DealViewModel by viewModels()
    private lateinit var serviceToDeleteId: String


    private var dealsToDelete = ArrayList<Deal>()

    /**
     * Borra un servicio
     * @param idService id del servicio
     */
    fun deleteService(idService: String) {
        serviceToDeleteId = idService

        //Obtenemos un listado con todos los tratos y a partir de ello borramos todos los servicios relacionados a estos ultimos.
        // Orden de borrado --> HistoryDeals -> Deal -> Service -> Referencia del servicio en el usuario.
        dealsViewModel.getDeals()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilUsuario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): PerfilUsuarioServicios {
            val servicesId = ArrayList<UserServices>()
            return PerfilUsuarioServicios(servicesId = servicesId).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
    }
}