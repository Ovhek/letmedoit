package cat.copernic.letmedoit.General.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.letmedoit.General.model.CategoryProvider
import cat.copernic.letmedoit.General.model.adapter.CategoryAdapter
import cat.copernic.letmedoit.General.viewmodel.SearchViewViewModel
import cat.copernic.letmedoit.LISTASDEPRUEBA
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.databinding.FragmentHomeCategoriesListBinding
import cat.copernic.letmedoit.databinding.FragmentSearchViewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeCategoriesList.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeCategoriesList : Fragment(){
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

    lateinit var binding : FragmentHomeCategoriesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeCategoriesListBinding.inflate(inflater,container,false)

        //Inicializamos el RecyclerView
        inicializarRecyclerView()
        return binding.root
    }

    /**
     * Inicializa el RecyclerView
     * */
    private fun inicializarRecyclerView() {

        val categoryRecyclerView = binding.categoryRecycleView
        //LinearLayoutManager HORIZONTAL
        categoryRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL,false)
        //Asignación del adaptador al recyclerview.
        categoryRecyclerView.adapter = CategoryAdapter(CategoryProvider.obtenerCategorias())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeCategoriesList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeCategoriesList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}