package cat.copernic.letmedoit.presentation.view.general.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.databinding.FragmentFiltroCategoriasBinding
import cat.copernic.letmedoit.presentation.viewmodel.admin.CreateCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [FiltroCategorias.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FiltroCategorias : Fragment() {

    lateinit var binding : FragmentFiltroCategoriasBinding
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

    private val categoryViewModel : CreateCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFiltroCategoriasBinding.inflate(inflater, container, false)
        binding.btnDone.isEnabled = false

        categoryViewModel.getCategories()
        initObserver()
        initListeners()
        return binding.root
    }

    private lateinit var categoryList : List<Category>
    private lateinit var  categoryNameList : List<String>

    private fun initListeners() {
        binding.backArrow.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnDone.setOnClickListener{ applyFilter() }

        //Al seleccionar un item del spinner de categorias
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val subcategoryNameList = ArrayList(categoryList[position].subcategories.map { it.nombre }.toList())
                Utils.AsignarPopUpSpinner(requireContext(), subcategoryNameList,binding.spinnerSubcategory)
            }
        }
    }

    private fun applyFilter() {
        val action = FiltroCategoriasDirections
            .filterBackToHome(binding.spinnerOrderby.selectedItemPosition, Category(nombre = binding.spinnerCategory.selectedItem.toString(), subcategories = arrayListOf(Subcategory(nombre = binding.spinnerSubcategory.selectedItem.toString()))))
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun initObserver() {
        categoryViewModel.getCategoriesState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<Category>> -> {
                    binding.btnDone.isEnabled = true
                    categoryList = dataState.data
                    initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("Error: ",requireContext(),dataState.exception.message.toString())
                }
                is DataState.Loading -> {  }
                else -> Unit
            }
        } )
    }

    private fun initView(){
        categoryNameList = categoryList.map { it.nombre }.toList()
        Utils.AsignarPopUpSpinner(requireContext(), categoryNameList as ArrayList<String>,binding.spinnerCategory)
        val orderByList = ArrayList<String>()
        orderByList.addAll(listOf("Service Name","Date (Newest)","Date (Oldest)"))
        Utils.AsignarPopUpSpinner(requireContext(), orderByList,binding.spinnerOrderby)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FiltroCategorias.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FiltroCategorias().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}