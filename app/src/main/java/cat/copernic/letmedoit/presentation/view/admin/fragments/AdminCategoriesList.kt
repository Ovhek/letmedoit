package cat.copernic.letmedoit.presentation.view.admin.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.presentation.viewmodel.admin.CreateCategoryViewModel
import cat.copernic.letmedoit.presentation.adapter.admin.AdminCategoryAdapter
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.databinding.FragmentAdminCategoriesListBinding
import com.bumptech.glide.Glide.init
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Fragment que infla y gestiona las categorias de la aplicacion
 * Muestra todas la categorias en un recycler view
 * Permite elimnar, editar y crear categorias y subcategorias
 * Utilza ViewModel para comunicarse con el repositorio de categorias
 */
@AndroidEntryPoint
class AdminCategoriesList : Fragment() {

    /*Declaración y incilizacion de variables*/

    //Binding
    private var _binding: FragmentAdminCategoriesListBinding? = null
    private val binding get() = _binding!!

    //BD
    private val viewModel: CreateCategoryViewModel by viewModels()

    //RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryMutableList: MutableList<Category>
    private lateinit var adapter: AdminCategoryAdapter
    private lateinit var llmanager: LinearLayoutManager

    //Otras views
    private lateinit var barraBusqueda: android.widget.SearchView
    lateinit var dialogBinding: View
    lateinit var myDialog: Dialog

    /*Metodos principales*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //inflamos la vista utilizando binding
        _binding = FragmentAdminCategoriesListBinding.inflate(inflater, container, false)

        //asignamos valores al las variables que referecnian las vistas
        llmanager = LinearLayoutManager(binding.root.context)
        recyclerView = binding.rcvListaCategorias
        barraBusqueda = binding.searchViewAdminCategories


        init()//iniciar y cargar datos necerarios


        //Listenners
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnAdd.setOnClickListener { createCategory() }


        //Obervers
        viewModel.newCategoryState.observe(
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

        viewModel.deleteCategoryState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { dataState ->
                when (dataState) {
                    is DataState.Success<Boolean> -> {
                        Log.d("AdminCategories", dataState.data.toString())
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

        viewModel.getCategoriesState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        categoryMutableList = dataState.data.toMutableList()
                        finishLoadingProcess()
                        initRecyclerView()


                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        finishLoadingProcess()

                    }
                    is DataState.Loading -> {
                        showProgressLoadCategories()
                    }
                    else -> Unit
                }
            })





        return binding.root
    }

    /*Metodos privados*/
    /**
     * Cierra el dialogo mostrado actualmente
     */
    private fun finishedProgress() {
        //Hace: Finaliza el popup
        myDialog.dismiss()
    }

    /**
     * Muestra la animacion de carga al eliminar una categoria
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
     * Muestra la animacion de carga al miestras carga las categorias
     */
    private fun showProgressLoadCategories() {
        //Hace: Descactiva todas las vista y muestra una barra de progreso
        binding.categoryLoading.isVisible = true
        binding.btnAdd.isEnabled = false
        binding.searchViewAdminCategories.isEnabled = false


    }

    /**
     * Oculta la animacion de carga
     */
    private fun finishLoadingProcess() {
        //Hace: Activa todas las vistas y descactiva la barra de progreso
        binding.categoryLoading.isVisible = false
        binding.categoryLoading.isEnabled = false
        binding.btnAdd.isEnabled = true
        binding.searchViewAdminCategories.isEnabled = true
    }

    /**
     * Crea una categoria y la guarda en la base de datos
     */
    private fun createCategory() {
        //Hace: Crear una categoria, la sube a la bd y refresca el recycler view

        dialogBinding = layoutInflater.inflate(R.layout.create_category_dialog, null)
        myDialog = Dialog(binding.root.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
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
                val category = creteCategoryF(name, desc)
                viewModel.insertCategory(category)
                categoryMutableList.add(index = 0, category)
                adapter.notifyItemInserted(0)
                llmanager.scrollToPositionWithOffset(0, 10)
            }
        }


    }

    /**
     * Inicializa el recycler view
     */
    private fun initRecyclerView() {
        //hace: Inicia y configura el recyclerView
        adapter = AdminCategoryAdapter(categoryList = categoryMutableList,
            onClickListener = { category, position -> onItemSelected(category, position) },
            onClickDelete = { position -> onDeletedItem(position) },
            onClickEdit = { category -> onEditItem(category) })
        recyclerView.layoutManager = llmanager
        recyclerView.adapter = adapter
    }

    /**
     * Muestra la informacion del la categoria pulsada
     * @param category Category
     * @param position Posicion de la categoria en el recycler view
     */
    private fun onItemSelected(category: Category, position: Int) {
        //hace: muestra un popup con la descripcion correspondiente de la categoria pulsada
        val dialogBinding = layoutInflater.inflate(R.layout.show_category_description_dialog, null)
        val myDialog = Dialog(binding.root.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        dialogBinding.findViewById<TextView>(R.id.textInput_categoryName).text = category.nombre
        dialogBinding.findViewById<TextView>(R.id.textInput_categoryDesc).text =
            category.description
        myDialog.show()

        dialogBinding.findViewById<Button>(R.id.btn_closse_dialog).setOnClickListener {
            myDialog.dismiss()
        }
        dialogBinding.findViewById<Button>(R.id.btn_save).setOnClickListener {
            val txtInput_name =
                dialogBinding.findViewById<TextInputEditText>(R.id.textInput_categoryName)
            val txtInput_desc =
                dialogBinding.findViewById<TextInputEditText>(R.id.textInput_categoryDesc)
            val name = txtInput_name.text.toString().trim()
            val desc = txtInput_desc.text.toString().trim()

            if (!name.isEmpty() && !name.isBlank() && !desc.isEmpty() && !desc.isBlank()) {
                category.nombre = name
                category.description = desc
                viewModel.updateName(category.id, category.nombre)
                viewModel.updateDesc(category.id, category.description)

                categoryMutableList.set(position, category)
                adapter.notifyItemChanged(position)
                myDialog.dismiss()
            }
        }

    }

    /**
     * Edita una categoria
     * @param category Category
     */
    private fun onEditItem(category: Category) {
        //hace: dirige al usario a un fragment con las subcategorias correspodientes
        val action = AdminCategoriesListDirections.actionAdminCategoriesListToAdminSubcategoryList(
            subcategories = category.subcategories.toTypedArray(),
            categoryId = category.id
        )
        findNavController().navigate(action)
    }

    /**
     * Elimina un categoria
     * @param position Indice de la categoria en el recycler view
     */
    private fun onDeletedItem(position: Int) {
        //hace: elimina una categoria
        dialogBinding = layoutInflater.inflate(R.layout.delete_category_alert_dialog, null)
        myDialog = Dialog(binding.root.context)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.show()

        val btn_cancel = dialogBinding.findViewById<Button>(R.id.btn_cancelDeleteCategory)
        val btn_accept = dialogBinding.findViewById<Button>(R.id.btn_acceptDeleteCategory)

        btn_cancel.setOnClickListener { myDialog.dismiss() }

        btn_accept.setOnClickListener {
            Log.d("Admin", categoryMutableList.get(position).id)
            viewModel.deleteCategory(categoryMutableList.get(position).id)
            categoryMutableList.removeAt(position)
            adapter.notifyItemRemoved(position)

        }

    }

    /**
     * Crea un instancia de Category
     * @return Category
     */
    private fun creteCategoryF(name: String, desc: String): Category {
        //hace: crear una categoria
        //return: devuelve una categoria
        return Category(
            name,
            desc,
            arrayListOf<Subcategory>(),
            "favorites_icon",
        )
    }

    /**
     * Inicia la lectura de base de datos de todas las categorias
     */
    private fun init() {
        //hace: lee toda las categorias de la base de datos
        viewModel.getCategories()
    }


}