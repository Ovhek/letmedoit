package cat.copernic.letmedoit.presentation.view.general.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.UserFavoriteProfiles
import cat.copernic.letmedoit.Utils.datahepers.UserServices
import cat.copernic.letmedoit.data.model.*
import cat.copernic.letmedoit.databinding.FragmentPerfilUsuarioMenuSuperiorBinding
import cat.copernic.letmedoit.presentation.adapter.general.UserTopMenuAdapter
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmen que muestra el menu superior de un perfil
 */
@AndroidEntryPoint
class PerfilUsuarioMenuSuperior : Fragment() {
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
    private val args: PerfilUsuarioMenuSuperiorArgs by navArgs()
    private var isUserFav = false
    lateinit var adapter : FragmentStateAdapter
    lateinit var binding : FragmentPerfilUsuarioMenuSuperiorBinding
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPerfilUsuarioMenuSuperiorBinding.inflate(inflater,container,false)

        initObservers()

        userViewModel.getUser(args.userID)
        if(Constants.USER_LOGGED_IN_ID != "") userViewModel.getFavoriteProfiles()


        if(FirebaseAuth.getInstance().currentUser == null || args.userID == Constants.USER_LOGGED_IN_ID){
            binding.btnFavorites.visibility = View.INVISIBLE
            binding.btnReport.visibility = View.INVISIBLE
        }
        binding.btnFavorites.setOnClickListener { manageUserFavorite() }

        binding.btnBack.setOnClickListener{ requireActivity().onBackPressed() }
        //Evento que se llama al empezar el drawing de la vista. Obtenemos el height del movil, luego el del Menu superior los restamos y lo utilizamos como height del viewpager. luego actualizamos el layout de nuevo
        var viewPagerHeightSize = 0
        binding.viewPager.viewTreeObserver.addOnGlobalLayoutListener( object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.viewPager.viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.getDefaultDisplay().getMetrics(displayMetrics)
                val heightPhone = displayMetrics.heightPixels

                //val width = displayMetrics.widthPixels
                val heightTopMenu = binding.topMenuUser.measuredHeight
                viewPagerHeightSize = heightPhone-(heightTopMenu)
                binding.viewPager.layoutParams.height = viewPagerHeightSize - 50
                binding.viewPager.requestLayout()
            }

        })

        return binding.root
    }

    /**
     * Inserta o elimna el perfil de favoritos
     */
    private fun manageUserFavorite() {
        if (!isUserFav) userViewModel.addFavoriteProfile(args.userID)
        else userViewModel.deleteFavoriteProfile(args.userID)
        isUserFav = !isUserFav
        manageFavIcon()
    }

    /**
     * Gestiona el icono de me gusta del perfil
     */
    private fun manageFavIcon(){
        if (!isUserFav)binding.btnFavorites.background = ContextCompat.getDrawable(binding.root.context, R.drawable.favorites_ion_colored)
        else binding.btnFavorites.background = ContextCompat.getDrawable(binding.root.context, R.drawable.ic_round_favorite_24)
    }
    private lateinit var user : Users

    /**
     * Inicia los observers
     */
    private fun initObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Users?> -> {
                    user = dataState.data!!
                    userViewModel.getServices(args.userID)
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        userViewModel.getServicesState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<ArrayList<UserServices>> -> {

                    user.servicesId = dataState.data
                    userViewModel.getOpinions(args.userID)
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
                    user.opinions.addAll(dataState.data)
                    initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
        userViewModel.getFavoriteProfilesState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<ArrayList<UserFavoriteProfiles>> -> {
                    isUserFav = (dataState.data.contains(UserFavoriteProfiles(args.userID)))
                    manageFavIcon()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                }
                is DataState.Loading -> {
                }
                else -> Unit
            }
        } )
        userViewModel.addFavoriteProfileState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    binding.btnFavorites.isEnabled = true
                    isUserFav = true
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                    binding.btnFavorites.isEnabled = true
                }
                is DataState.Loading -> {
                    binding.btnFavorites.isEnabled = false
                }
                else -> Unit
            }
        } )
        userViewModel.deleteFavoriteProfileState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    binding.btnFavorites.isEnabled = true
                }
                is DataState.Error -> {
                    Utils.showOkDialog("${resources.getString(R.string.error)}",requireContext(),dataState.exception.message.toString(),requireActivity())
                    binding.btnFavorites.isEnabled = true
                }
                is DataState.Loading -> {
                    binding.btnFavorites.isEnabled = false
                }
                else -> Unit
            }
        } )
    }

    @SuppressLint("SetTextI18n")
    /**
     * Inicia la vista
     */
    private fun initView() {


        if(user.avatar != "") Picasso.get().load(user.avatar).into(binding.profileImage)
        binding.userRating.rating = user.rating
        binding.ratingNum.text = "(${DecimalFormat("#.##").format(user.rating)})"
        binding.profileNameSurname.text = "${user.name} ${user.surname} \n @${user.username}"
        //Fragmentos del TabLayout
        val fragments : ArrayList<Fragment> = arrayListOf(
            PerfilUsuarioServicios(user.servicesId),
            OpinionsUser(user.opinions),
            ProfileMoreInfo(user)
        )
        //Adapter del ViewPager
        adapter = UserTopMenuAdapter(this.childFragmentManager,fragments,lifecycle)
        //Lo asignamos
        binding.viewPager.adapter = adapter

        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        TabLayoutMediator(binding.tablayoutUserProfile, binding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                //Configure tabs..
                when (position) {
                    0 -> { tab.text = "${resources.getText(R.string.txt_services)}"}
                    1 -> { tab.text = "${resources.getText(R.string.txt_num_opinions)}"}
                    2 -> { tab.text = "${resources.getText(R.string.txt_num_info)}"}
                }
            }).attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReport.setOnClickListener{ Utils.goToUserReport(view, args.userID) }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerfilUsuarioMenuSuperior.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PerfilUsuarioMenuSuperior().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}