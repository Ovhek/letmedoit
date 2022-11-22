package cat.copernic.letmedoit.General.view.fragments

import cat.copernic.letmedoit.databinding.FragmentMenuInferiorBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.size
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import cat.copernic.letmedoit.General.view.activities.Home
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.transition.MaterialFadeThrough
import com.google.errorprone.annotations.Var
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [Menu_Inferior.newInstance] factory method to
 * create an instance of this fragment.
 */
class Menu_Inferior : Fragment() {
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

    lateinit var bottomNavigation : BottomNavigationView
    lateinit var navController : NavController

    lateinit var binding : FragmentMenuInferiorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuInferiorBinding.inflate(inflater,container,false)

        bottomNavigation = binding.menuInferior
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = (this.parentFragmentManager.findFragmentById(R.id.navController) as NavHostFragment).navController

        bottomNavigation.setupWithNavController(navController)

        //Muestra o oculta la barra de navegación dependiendo el destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
           when(destination.id){
               R.id.homeFragment,
               R.id.registroOpcionesCuenta ,
               R.id.messagesVis ,
               R.id.uploadServicesVis ,
               R.id.profiles_services_manager_vis  -> showBottomNav()
               R.id.verListadoFavServices,
               R.id.newService,
               R.id.verConversaciones,
               R.id.verListadoDeals,
               R.id.accountOptions-> showBottomNav()
               else -> hideBottomNav()
           }
        }
        if(FirebaseAuth.getInstance().currentUser != null){
            binding.menuInferior.menu.clear() //clear old inflated items.
            binding.menuInferior.inflateMenu(R.menu.menu_principal_logeado);
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showBottomNav() {
        bottomNavigation.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigation.visibility = View.GONE

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Menu_Inferior.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Menu_Inferior().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}