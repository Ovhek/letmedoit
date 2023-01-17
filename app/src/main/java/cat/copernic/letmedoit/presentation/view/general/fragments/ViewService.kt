package cat.copernic.letmedoit.presentation.view.general.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.UserConstants
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentViewServiceBinding
import cat.copernic.letmedoit.presentation.adapter.general.SliderImagesAdapter
import cat.copernic.letmedoit.presentation.view.users.fragments.CreateDealDirections
import cat.copernic.letmedoit.presentation.view.visitante.activities.Login
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat


const val TAG_SLIDER_IMAGES = "sliderCardView"

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que inflai y gestiona la vista de  un servicio
 * Utilza el ViewModel para comunicarse con los repositorios correspondientes
 */
@AndroidEntryPoint
class viewService : Fragment() {
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

    lateinit var adapter: SliderImagesAdapter
    lateinit var binding: FragmentViewServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val userViewModel: UserViewModel by viewModels()
    private val args: viewServiceArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            binding.btnFav.visibility = View.INVISIBLE
            binding.btnReport.visibility = View.INVISIBLE
        }
        initView(args.service)
        initListeners()
        initObservers()

    }

    /**
     * Se encarga de actualiza el icono de me gusta (corazon) de los servicios segune l usuario
     */
    private fun changeFavIcon() {
        if (args.service.defaultFav) binding.btnFav.background =
            ContextCompat.getDrawable(binding.root.context, R.drawable.ic_round_favorite_24)
        else binding.btnFav.background =
            ContextCompat.getDrawable(binding.root.context, R.drawable.favorites_ion_colored)
    }

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.btnGoToProfile.setOnClickListener {
            goToUserProfile(
                requireView(),
                args.service.userid
            )
        }
        binding.btnChat.setOnClickListener { goToCreateDeal() }
        binding.btnReport.setOnClickListener {
            Utils.goToUserReport(
                requireView(),
                args.service.userid
            )
        }
        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnFav.setOnClickListener { manageFavorite() }
        binding.btnEdit.setOnClickListener { gotToEditService() }
    }

    /**
     * Envia al usuario a editar el servicio
     */
    private fun gotToEditService() {
        val action = viewServiceDirections.actionViewServiceToNewService(args.service.id)
        Navigation.findNavController(requireView()).navigate(action)
    }

    /**
     * Envia al usuario a crear un trato
     */
    private fun goToCreateDeal() {


        if (Constants.USER_LOGGED_IN_ID == "") startActivity(
            Intent(
                requireContext(),
                Login::class.java
            )
        )
        else {
            val action = viewServiceDirections.actionViewServiceToCreateDeal(
                Constants.USER_LOGGED_IN,
                user,
                args.service
            )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    /**
     * Ingresa el servicio a la lista de favoritos
     */
    private fun manageFavorite() {


        val service = args.service
        service.defaultFav = !service.defaultFav
        changeFavIcon()

        if (service.defaultFav) userViewModel.addFavoriteService(service.id)
        else userViewModel.deleteFavoriteService(service.id)
    }

    private lateinit var user: Users

    /**
     * Inicia los obsevers que monitorizan el proceso de las operaciones con la base de datos
     */
    private fun initObservers() {
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    initView(dataState.data)
                }
                is DataState.Error -> {
                    requireActivity().onBackPressed()
                }
                is DataState.Loading -> {}
                else -> {}
            }
        })
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users?> -> {
                    user = dataState.data!!
                    if (user.avatar != "") Picasso.get().load(user.avatar)
                        .into(binding.profileImage)
                    binding.userRating.rating = user.rating
                    binding.ratingNum.text = "(${DecimalFormat("#.##").format(user.rating)})"
                    if (user.name != "") binding.nameSurname.text = "${user.name} ${user.surname}"
                    else binding.nameSurname.text = "@${user.username}"
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
        userViewModel.addFavoriteServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    UserConstants.USER_FAVORITE_SERVICES_IDS.add(args.service.id)
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
        userViewModel.deleteFavoriteServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    UserConstants.USER_FAVORITE_SERVICES_IDS.remove(args.service.id)
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

    /**
     * Envia al usuario al perfil del dueño del servicio
     */
    private fun goToUserProfile(view: View, userId: String) {
        val action = viewServiceDirections.viewServiceToUserProfile(userID = userId)
        view.findNavController().navigate(action)
    }

    /**
     * Inicia la vista
     */
    private fun initView(service: Service) {

        if (Constants.USER_LOGGED_IN_ID == "") binding.btnChat.text =
            resources.getString(R.string.sign_in_to_make_deals)

        if (Constants.USER_LOGGED_IN_ID == args.service.userid) {
            binding.btnFav.isVisible = false
            binding.btnReport.isVisible = false
            binding.btnChat.isVisible = false
            binding.btnEdit.isVisible = true
        }
        if (UserConstants.USER_FAVORITE_SERVICES_IDS.contains(service.id)) service.defaultFav = true
        changeFavIcon()
        binding.tittleService.text = service.title
        binding.subTextCategory.text = service.category.id_category
        binding.subTextSubCategory.text = service.category.id_subcategory
        binding.descriptionService.text = service.description
        binding.txtCountFav.text = service.n_likes.toString()
        binding.txtEditedTime.text = service.edited_time
        userViewModel.getUser(service.userid)
        adapter = SliderImagesAdapter(service.image)
        createSliderDots(service.image)


        //Eventos del ViewPager de imagenes
        binding.imageServiceViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            var sumPosAndOffset: Float = 0.0f
            var swiped = true
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                var rePos = position

                if (swiped) {
                    //position + offset > sumPositios --> Swipe Izquierda a Derecha y Viceversa
                    if (position + positionOffset > sumPosAndOffset && positionOffset > 0)
                        rePos++

                    if (rePos == service.image.size)
                        rePos--

                    changeColor(rePos)
                    swiped = false

                }

                super.onPageScrolled(rePos, positionOffset, positionOffsetPixels)
                sumPosAndOffset = positionOffset + position
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                swiped = true
            }

        })

        binding.imageServiceViewPager.adapter = adapter
    }

    /**
     * Por cada foto crea un punto gris debajo de la imagen utilizando cardviews
     */
    private fun createSliderDots(images: ArrayList<Image>) {
        var contador = 0
        binding.SliderDots.removeAllViews()
        images.forEach { _ ->
            contador++
            val cardViewDotContainer = CardView(requireContext())
            val layoutparams = LinearLayout.LayoutParams(20, 20)
            layoutparams.setMargins(10)
            cardViewDotContainer.layoutParams = layoutparams
            cardViewDotContainer.radius = 20F
            cardViewDotContainer.elevation = 2F
            cardViewDotContainer.contentDescription = TAG_SLIDER_IMAGES
            cardViewDotContainer.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.divider_color
                )
            )
            binding.SliderDots.addView(cardViewDotContainer)
        }
    }

    private val serviceViewModel: ServiceViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        serviceViewModel.getService(args.service.id)
    }


    private var lastColored: CardView? = null

    /**
     * Cambia el color del CardView a azul o a gris
     */
    fun changeColor(position: Int) {

        val outputCardViews = ArrayList<View>()
        binding.root.findViewsWithText(
            outputCardViews,
            TAG_SLIDER_IMAGES,
            View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION
        )

        lastColored?.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.secundario_gris
            )
        )
        (outputCardViews[position] as CardView).setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.principal_blanco
            )
        )

        lastColored = outputCardViews[position] as CardView

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment viewService.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            viewService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}