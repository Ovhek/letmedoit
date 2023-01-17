package cat.copernic.letmedoit.presentation.view.general.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Opinion
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentOpinionsUserBinding
import cat.copernic.letmedoit.presentation.adapter.general.OpinionsAdapter
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import dagger.hilt.android.AndroidEntryPoint



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que muestra las opninones de un usuario
 */
@AndroidEntryPoint
class OpinionsUser(private val opinions: ArrayList<Opinion>) : Fragment() {
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

    private val userViewModel : UserViewModel by viewModels()
    private val serviceViewModel : ServiceViewModel by viewModels()
    private val users = ArrayList<Users>()
    private val services = ArrayList<Service>()
    lateinit var opinionsRecyclerView : RecyclerView
    lateinit var adapter : OpinionsAdapter
    lateinit var binding : FragmentOpinionsUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        users.clear()
        services.clear()
        binding = FragmentOpinionsUserBinding.inflate(layoutInflater,container,false)
        initObservers()
        opinions.forEach {
            userViewModel.getUser(it.userId)
        }
        return binding.root
    }

    /**
     * Inicia los observers
     */
    private fun initObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Users?> -> {
                    dataState.data?.let { users.add(it) }
                    if(users.size == opinions.size)
                        opinions.forEach {
                            serviceViewModel.getService(it.serviceId)
                        }
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),
                        requireActivity()
                    )
                }
                is DataState.Loading -> {
                }
                else -> Unit
            }
        } )
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Service> -> {
                    services.add(dataState.data)
                    if(services.size == opinions.size) initRecyclerView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {
                }
                else -> Unit
            }
        } )
    }

    /**
     * Inicia el recycelr view que muestra las opniones
     */
    private fun initRecyclerView() {

        opinionsRecyclerView = binding.RecyclerOpinions
        //LinearLayoutManager HORIZONTAL
        //serviceRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL,false)
        opinionsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
        //Asignación del adaptador al recyclerview.

        opinionsRecyclerView.setHasFixedSize(true)
        adapter = OpinionsAdapter(opinions,users,services)
        opinionsRecyclerView.adapter = adapter

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment opinions_user.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): OpinionsUser {
            val opinions = ArrayList<Opinion>()
            return OpinionsUser(opinions).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
    }
}