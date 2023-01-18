package cat.copernic.letmedoit.presentation.view.admin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.presentation.view.general.fragments.*
import cat.copernic.letmedoit.databinding.FragmentAdminViewUsersBinding
import cat.copernic.letmedoit.presentation.adapter.general.UsersAdapter
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.bumptech.glide.Glide.init
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment que muestra todos los usuarios y permite banearlos
 */
@AndroidEntryPoint
class admin_view_users : Fragment() {


    private var _binding: FragmentAdminViewUsersBinding? = null
    private val binding get() = _binding!!
    private var usersList = ArrayList<Users>()
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UsersAdapter
    private lateinit var btnBan: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminViewUsersBinding.inflate(inflater, container, false)


        init()


        viewModel.getAllUserState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        usersList =
                            ArrayList(dataState.data)
                        initRecyclerView()
                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )


                    }
                    is DataState.Loading -> {

                    }
                    else -> Unit
                }
            })



        btnBan = binding.flbuttonBan


        binding.btnFlechaBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btnBan.setOnClickListener {
            banearUsuarios()
        }





        return binding.root
    }

    /**
     * Banea a los usuarios seleccionados en el recycler view
     */
    private fun banearUsuarios() {

        usersList.forEachIndexed { i, user ->
            if (user.check == true) {
                if (user.banned == true) {
                    viewModel.updateBan(user.id, false)
                    usersList.get(i).banned = false
                } else {
                    viewModel.updateBan(user.id, true)
                    usersList.get(i).banned = true
                }
                adapter.notifyItemChanged(i)

            }
        }
    }

    /**
     * Inicializa el recycler view
     */
    fun initRecyclerView() {
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(binding.root.context)
        adapter =
            UsersAdapter(usersList, onClickCheckBox = { user -> checkTheBox(user) })

        binding.recyclerViewUsers.adapter = adapter
    }

    /**
     * Cambia el estado del check box de el usuario
     * @param user Users
     */
    private fun checkTheBox(user: Users) {
        user.check = !user.check
    }

    /**
     * Inicia la lectura de todos los usuarios de la base de datos
     */
    private fun init() {
        viewModel.getAllUser()
    }

}