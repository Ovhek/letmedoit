package cat.copernic.letmedoit.presentation.view.users.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.UserFavoriteProfiles
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.presentation.adapter.users.FavUsersAdapter
import cat.copernic.letmedoit.databinding.FragmentViewFavUsersBinding
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmeng que infla y gestiona la pantalla de perfiles favoritos de un usuario
 * Utiliza el UserViewModel para comunicarse con el repositorio
 */
@AndroidEntryPoint
class viewFavUsers : Fragment() {

    private var _binding: FragmentViewFavUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //ViewModel que comunica el repositorio con el fragment
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewFavUsersBinding.inflate(inflater, container, false)
        //initRecyclerView()

        //Leemos los perfiles favoritos el usuario de la base de datos
        userViewModel.getFavoriteProfiles()

        //Inciamos los obervers que observan el estado de las operaciones con la base de datos
        initObservers()
        return binding.root
    }

    //Numero de usuarios favoritos
    private var totalFavUsers = 0

    //Numero de usuarios favoritos leidos de la abse de datos
    private var obtainedFavUsers = 0

    //Lista de usurios favoritos
    private var users = ArrayList<Users>()

    /**
     * Inicia los obervers
     */
    private fun initObservers() {
        //Monitoriza el estado de lectura de los perfiles favortios de la base de datos
        userViewModel.getFavoriteProfilesState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<ArrayList<UserFavoriteProfiles>> -> {
                    if (::adapter.isInitialized) adapter.clear()

                    totalFavUsers = dataState.data.size
                    if (totalFavUsers == 0) hideProgress()

                    dataState.data.forEach { userViewModel.getUser(it.favorite_profile_id) }
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
        //Monitoriza el estado de lectura de un usuario de la base de datos
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users?> -> {
                    if (dataState.data != null) {
                        users.add(dataState.data)
                        obtainedFavUsers++
                    }
                    if (obtainedFavUsers == totalFavUsers) initRecyclerView(users)
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
                }
                else -> Unit
            }
        })
        //Monitoriza el estado de elimnacion de un perfil de la subcoleccion de favoritos
        //del usuario
        userViewModel.deleteFavoriteProfileState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
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
    }

    /**
     *Oculta la animacion de carga
     */
    private fun hideProgress() {
        binding.loadingFavUsers.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        binding.loadingFavUsers.isVisible = true
    }

    /**
     * Elimina un perfil de la subcoleccion de favoritos
     */
    fun deleteFavoriteProfile(idProfile: String) {
        userViewModel.deleteFavoriteProfile(idProfile)
    }


    private lateinit var adapter: FavUsersAdapter
    //Inicia el recycler view
    fun initRecyclerView(users: ArrayList<Users>) {
        hideProgress()
        binding.recyclerViewFavUser.layoutManager = GridLayoutManager(binding.root.context, 2)
        adapter = FavUsersAdapter(users, this)
        binding.recyclerViewFavUser.adapter = adapter
        obtainedFavUsers = 0
    }
}