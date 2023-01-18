package cat.copernic.letmedoit.presentation.view.admin.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.presentation.adapter.admin.AdminSubcategoryAdapter
import cat.copernic.letmedoit.databinding.FragmentAdminSubcategoryListBinding
import cat.copernic.letmedoit.presentation.viewmodel.admin.CreateCategoryViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text

/**
 * Fragment que muestra la susbcategorias
 * Utiliza ViewModel para comunicarse con el repositorio de categorias.
 * Permite crear y eliminar subcategorias
 */
@AndroidEntryPoint
class AdminSubcategoryList : Fragment() {

    private var _binding: FragmentAdminSubcategoryListBinding? = null
    private val binding get() = _binding!!
    val args: AdminSubcategoryListArgs by navArgs()
    lateinit private var categoryID: String
    private val viewModel: CreateCategoryViewModel by viewModels()
    lateinit private var subcateogryMutableList: MutableList<Subcategory>
    private lateinit var adapter: AdminSubcategoryAdapter
    private lateinit var llmanager: LinearLayoutManager
    lateinit var dialogBinding: View
    lateinit var myDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminSubcategoryListBinding.inflate(inflater, container, false)
        llmanager = LinearLayoutManager(binding.root.context)
        subcateogryMutableList = args.subcategories.toMutableList()
        categoryID = args.categoryId
        initRecylerView()

        viewModel.insertCategoryState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { dataState ->
                when (dataState) {
                    is DataState.Success<Boolean> -> {
                        finishedProgress()
                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        finishedProgress()
                    }
                    is DataState.Loading -> {
                        showProgress()
                    }
                    else -> Unit
                }
            })

        viewModel.deleteSubcategoryState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { dataState ->
                when (dataState) {
                    is DataState.Success<Boolean> -> {
                        finishedProgress()
                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        finishedProgress()
                    }
                    is DataState.Loading -> {
                        showDeleteProgress()
                    }
                    else -> Unit
                }

            })



        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnAdd.setOnClickListener {
            createSubcategory()
        }
        return binding.root
    }

    /**
     * Elimina la animacion de carga al elimnar un subcategoria
     */
    private fun showDeleteProgress() {
        val btnAccept = dialogBinding.findViewById<Button>(R.id.btn_acceptDeleteCategory)
        val btnCancel = dialogBinding.findViewById<Button>(R.id.btn_cancelDeleteCategory)
        val loading = dialogBinding.findViewById<ProgressBar>(R.id.deleteCategoryLoading)
        btnAccept.isEnabled = false
        btnCancel.isEnabled = false
        loading.isVisible = true
    }

    /**
     * Crea un subcategoria
     */
    private fun createSubcategory() {
        //Hace: Crear una categoria, la sube a la bd y refresca el recycler view

        dialogBinding = layoutInflater.inflate(R.layout.create_category_dialog, null)
        myDialog = Dialog(binding.root.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        dialogBinding.findViewById<TextView>(R.id.txt_title).text = resources.getString(R.string.Newsubcategory)
        myDialog.show()

        val btn_cancel = dialogBinding.findViewById<Button>(R.id.btn_cancelCreateCategorie)
        btn_cancel.setOnClickListener {
            myDialog.dismiss()
        }

        val btn_done = dialogBinding.findViewById<Button>(R.id.btn_doneCreate)

        btn_done.setOnClickListener {
            val txtInput_name =
                dialogBinding.findViewById<TextInputEditText>(R.id.txtInput_categoryName)
            val txtInput_desc =
                dialogBinding.findViewById<TextInputEditText>(R.id.txtInput_categoryDesc)
            val name = txtInput_name.text.toString().trim()
            val desc = txtInput_desc.text.toString().trim()
            if (!name.isEmpty() && !name.isBlank() && !desc.isEmpty() && !desc.isBlank()) {
                val subcategory = createSubcat(name, desc)
                viewModel.insertSubategory(categoryID, subcategory)
                subcateogryMutableList.add(index = 0, subcategory)
                adapter.notifyItemInserted(0)
                llmanager.scrollToPositionWithOffset(0, 10)
            }
        }
    }
    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        //Hace: Muestra una barra de progresso y desactiva todos los contraloderes el popup
        val btnAccept = dialogBinding.findViewById<Button>(R.id.btn_doneCreate)
        val btnCancel = dialogBinding.findViewById<Button>(R.id.btn_cancelCreateCategorie)
        val dialogTextBox =
            dialogBinding.findViewById<TextInputEditText>(R.id.txtInput_categoryName)
        val loading = dialogBinding.findViewById<ProgressBar>(R.id.newCategoryLoading)
        btnAccept.isEnabled = false
        btnCancel.isEnabled = false
        dialogTextBox.isEnabled = false
        loading.isVisible = true

    }

    /**
     * Cierra el popuo que se este mostrando actualmente
     */
    private fun finishedProgress() {
        //Hace: Finaliza el popup
        myDialog.dismiss()
    }

    /**
     * Crea una instancia de Subcategory
     * @param name
     * @param desc
     * @return Subcategory
     */
    private fun createSubcat(name: String, desc: String): Subcategory {
        //hace: crear una categoria
        //return: devuelve una categoria
        return Subcategory(
            name,
            desc,
        )
    }

    /**
     * Borra un subcategoria de la base de datos y del recycler view
     * @param position Indice en la lista del RecyclerView
     */
    private fun onDeletedItem(position: Int) {
        //hace: elimina una categoria
        dialogBinding = layoutInflater.inflate(R.layout.delete_category_alert_dialog, null)
        myDialog = Dialog(binding.root.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        dialogBinding.findViewById<TextView>(R.id.txt_title_deleteCategory).text = resources.getString(R.string.DeleteSubcategory)
        myDialog.show()

        val btn_cancel = dialogBinding.findViewById<Button>(R.id.btn_cancelDeleteCategory)
        val btn_accept = dialogBinding.findViewById<Button>(R.id.btn_acceptDeleteCategory)

        btn_cancel.setOnClickListener { myDialog.dismiss() }

        btn_accept.setOnClickListener {
            viewModel.deleteSubcategory(categoryID,subcateogryMutableList.get(position).id)
            subcateogryMutableList.removeAt(position)
            adapter.notifyItemRemoved(position)

        }

    }

    /**
     * Inicializa el recycler view
     */
    private fun initRecylerView() {
        adapter =
            AdminSubcategoryAdapter(
                subcategoryList = subcateogryMutableList,
                onClickDelete = { position ->
                    onDeletedItem(position)
                })
        binding.rcvListaSubcategories.adapter = adapter
        binding.rcvListaSubcategories.layoutManager = llmanager
    }

}