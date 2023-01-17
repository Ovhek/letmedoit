package cat.copernic.letmedoit.presentation.view.general.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.databinding.FragmentSearchViewBinding
import cat.copernic.letmedoit.presentation.viewmodel.general.SearchViewViewModel


/**
 * Fragment que gestiona los servicios filtrados por el buscador
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchView : Fragment() {

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

    lateinit var searchViewQuery : String
    lateinit var binding : FragmentSearchViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchViewBinding.inflate(layoutInflater,container,false)

        binding.iconFilter.setOnClickListener{ goToFilter() }
        return binding.root
    }

    /**
     * Lleva a la pantalla de filtro
     */
    private fun goToFilter() {
        val action  = HomeFragmentDirections.actionHomeFragmentToFiltroCategoria()
        requireView().findNavController().navigate(action)
    }

    lateinit var model : SearchViewViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDestination = parentFragment?.findNavController()?.currentDestination?.label

        if(currentDestination != "fragment_home"){
            binding.iconFilter.isEnabled = false
            binding.iconFilter.isVisible = false
        }

        model = ViewModelProvider(requireActivity())[SearchViewViewModel::class.java]

        binding.iconFilter.setOnClickListener { Utils.goToDestination(requireView(),R.id.filtroCategorias) }

        binding.searchView.setOnQueryTextListener(
        object :  android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                model.sendQuery(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                model.sendQuery(newText.toString())
                return true
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchView.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchView().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
