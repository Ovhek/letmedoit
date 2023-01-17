package cat.copernic.letmedoit.presentation.view.users.fragments

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Selection.selectAll
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.databinding.FragmentNewServiceBinding
import cat.copernic.letmedoit.presentation.adapter.general.ImagesAdapter
import cat.copernic.letmedoit.presentation.viewmodel.admin.CreateCategoryViewModel
import cat.copernic.letmedoit.presentation.viewmodel.general.ServiceViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que infla y gestiona la pantalla para crear un nuevo servicio
 */
@AndroidEntryPoint
class NewService : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        getContent =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
                managePhotosUri(uriList)
            }
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            checkPermissions(
                permissions
            )
        }

    }

    var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        READ_EXTERNAL_STORAGE
    )
    private val args: NewServiceArgs by navArgs()
    private val serviceViewModel: ServiceViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var service: Service
    lateinit var getContent: ActivityResultLauncher<String>
    lateinit var binding: FragmentNewServiceBinding
    private val categoryViewModel: CreateCategoryViewModel by viewModels()
    private var editingImages = false
    private var editMode = false
    private lateinit var categoryList: List<Category>
    private lateinit var categoryNameList: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentNewServiceBinding.inflate(inflater, container, false)
        initListeners()
        categoryViewModel.getCategories()

        if (findNavController().previousBackStackEntry?.destination?.label == "fragment_create_deal")
            requireActivity().findViewById<BottomNavigationView>(R.id.menu_inferior).isVisible =
                false

        initRecyclerView()


        return binding.root
    }

    /**
     * Inicia los listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        binding.btnArrowBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.editDescription.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                binding.scrollableLayout.requestDisallowInterceptTouchEvent(true)
                return false
            }

        })
        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                /**
                 * Ejecuta la funcion respectiva con el item del spiner pulsado
                 */
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val subCategoryNames =
                        categoryList[position].subcategories.map { it.nombre } as ArrayList<String>
                    if (subCategoryNames.size <= 0) {
                        binding.txtTitleSubcategory!!.visibility = View.GONE
                        binding.containerSpinnerSubcategory.visibility = View.GONE
                    } else {
                        binding.txtTitleSubcategory!!.visibility = View.VISIBLE
                        binding.containerSpinnerSubcategory.visibility = View.VISIBLE
                    }
                    Utils.AsignarPopUpSpinner(
                        requireContext(),
                        subCategoryNames,
                        binding.spinnerSubcategory
                    )
                }

            }
        binding.btnAddImage.setOnClickListener { addImage() }
        binding.btnRemoveImage.setOnClickListener { removeImage() }
        binding.selectAll.setOnClickListener { selectAll() }
        binding.btnSave.setOnClickListener { saveService() }
    }

    /**
     * Inicia la vista con los datos del servicio correpondiente
     */
    private fun initViewWithData(service: Service) {
        binding.btnArrowBack.visibility = View.VISIBLE
        editMode = true
        binding.txtTitleChat.text = resources.getText(R.string.txt_edit_service)
        binding.editServiceTitle.setText(service.title)
        var categoryPos = 0
        var subCategoryPos = 0
        for (i in 0 until binding.spinnerCategory.adapter.count) {
            if (binding.spinnerCategory.getItemAtPosition(i).equals(service.category.id_category)) {
                categoryPos = i
                break
            }
        }
        for (i in 0 until binding.spinnerSubcategory.adapter.count) {
            if (binding.spinnerCategory.getItemAtPosition(i).equals(service.category.id_category)) {
                subCategoryPos = i
                break
            }
        }
        binding.spinnerCategory.setSelection(categoryPos)
        binding.spinnerSubcategory.setSelection(subCategoryPos)
        binding.editDescription.setText(service.description)
        imagesList.addAll(service.image)
        adapter.notifyDataSetChanged()

        requireActivity().findViewById<BottomNavigationView>(R.id.menu_inferior).isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    var totalImagesEdited = 0

    /**
     * Inicia los obervers que monitorizan las operaciones con la base de datos
     */
    private fun initObservers() {
        categoryViewModel.getCategoriesState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Category>> -> {
                    categoryList = dataState.data
                    val categoryNames = categoryList.map { it.nombre } as ArrayList<String>
                    Utils.AsignarPopUpSpinner(
                        requireContext(),
                        categoryNames,
                        binding.spinnerCategory
                    )
                    if (args.serviceID != "null")
                        serviceViewModel.getService(args.serviceID)
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.getServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    service = dataState.data
                    initViewWithData(service)
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
                    binding.btnSave.isEnabled = false
                }
                else -> binding.btnSave.isEnabled = true
            }
        })

        serviceViewModel.saveImageState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<String> -> {
                    if (editMode) editImageSuccess(dataState.data)
                    else uploadImageSuccess(dataState.data)
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.updateTitleState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    serviceViewModel.updateDescription(
                        service.id,
                        binding.editDescription.text.toString()
                    )
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
        serviceViewModel.updateDescriptionState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    serviceViewModel.updateEditedTime(
                        service.id,
                        LocalDate.now().format(DateTimeFormatter.ofPattern(("dd-MM-yyyy")))
                    )
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.updateEditedTimeState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    serviceViewModel.updateCategory(
                        service.id,
                        CategoryMap(
                            binding.spinnerCategory.selectedItem.toString(),
                            binding.spinnerSubcategory.selectedItem.toString()
                        )
                    )
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.updateCategoryState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    saveImages()
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.saveServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Service> -> {
                    userViewModel.addService(dataState.data.id)
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        serviceViewModel.removeImageUseCaseState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    removingImages = true
                    removeImageSuccess()
                    if (!editingImages && !removingImages) requireActivity().onBackPressed()
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })

        serviceViewModel.updateServiceImageState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<String> -> {
                    editingImages = true
                    editImageSuccess(dataState.data)
                    if (!editingImages && !removingImages) requireActivity().onBackPressed()
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })

        userViewModel.addServiceState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    hideProgress()
                    Utils.goToDestination(requireView(), R.id.homeFragment)
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
                is DataState.Loading -> {}
                else -> Unit
            }
        })

    }


    /**
     * Borra los campos del formulario
     */
    private fun resetComponents() {
        binding.editDescription.setText("")
        binding.editServiceTitle.setText("")
        numImgUploaded = 0
        imagesUploaded.removeAll(imagesUploaded)
        adapter.removeAll()
        adapter.notifyDataSetChanged()
    }

    /**
     * Oculta la animacion de carga
     */
    private fun hideProgress() {
        binding.btnSave.isEnabled = true
        binding.btnSave.text = resources.getString(R.string.save)
        binding.uploadServiceLoading.isVisible = false
    }

    /**
     * Muestra la animacion de carga
     */
    private fun showProgress() {
        binding.btnSave.isEnabled = false
        binding.btnSave.text = ""
        binding.uploadServiceLoading.isVisible = true
    }

    private var imagesToEdit = 0
    private var imagesToDelete = 0

    /**
     * Gurda las imagenes seleccionadas para el servicio
     */
    private fun saveImages() {
        if (adapter.getItems().size == 0) {
            Utils.showOkDialog(
                "Info:",
                requireContext(),
                resources.getString(R.string.noimageserror),
                requireActivity()
            )
            return
        }
        var index = 0
        if (args.serviceID != "null") {
            val notUploadedImages =
                adapter.getItems().filter { it.img_link.contains("content://") }.toList()
            imagesToEdit = notUploadedImages.size
            var lastID = adapter.getItems().filter { it.id.toInt() != 0 }.toList().last().id.toInt()
            notUploadedImages.forEach {
                lastID++
                serviceViewModel.editServiceImage(service.id, it.img_link.toUri(), lastID)
            }
            val deletedImages = service.image.minus(adapter.getItems().toSet())
            imagesToDelete = deletedImages.size
            deletedImages.forEach {
                serviceViewModel.removeImage(service.id, it.id.toInt(), it.img_link)
                totalImagesEdited--
            }
            return
        } else adapter.getItems().forEach {
            serviceViewModel.saveImage(it.img_link.toUri(), idServiceRandom, index)
            index++
        }

    }

    lateinit var idServiceRandom: String

    /**
     * Comprueva que los campos del formulario no esten vacios
     * @return false en caso de estar vacios y muestra una dialogo que avisa al usuario
     * @return true en caso de estar completos
     */
    private fun isDataSet(): Boolean {
        if (binding.editDescription.text.isNullOrEmpty()) {
            Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.nodescriptionerror),
                requireActivity()
            )
            return false
        }
        if (binding.editServiceTitle.text.isNullOrEmpty()) {
            Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.notitleerror),
                requireActivity()
            )
            return false
        }
        if (binding.listImages.size <= 0) {
            Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.noimageserror),
                requireActivity()
            )
            return false
        }

        return true

    }

    /**
     * Gurda el servicio
     */
    private fun saveService() {
        if (!isDataSet())
            return

        if (editMode) editService()
        else {
            idServiceRandom = UUID.randomUUID().toString()
            saveImages()
            showProgress()
        }

    }

    var numImgUploaded = 0
    var imagesUploaded = ArrayList<Image>()

    /**
     * Sube una imagen
     */
    private fun uploadImageSuccess(uri: String) {
        imagesUploaded.add(Image(numImgUploaded.toString(), uri, numImgUploaded))
        numImgUploaded++
        if (numImgUploaded >= adapter.itemCount)
            uploadService()

    }

    var numImgEdited = 0
    var imagesEdited = ArrayList<Image>()

    /**
     * Suben las nuevas imagenes
     */
    private fun editImageSuccess(uri: String) {
        imagesEdited.add(Image(numImgEdited.toString(), uri))
        numImgUploaded++
        if (numImgUploaded >= imagesToEdit) editingImages = false
    }

    var removingImages = false
    var numImagesRemoved = 0
    private fun removeImageSuccess() {
        numImagesRemoved++
        if (numImagesRemoved >= imagesToDelete) removingImages = false
    }

    /**
     * Edita el servicio
     */
    private fun editService() {
        //Editamos utilizando los observadores, para poder saber el progreso total. Orden de edición: Titulo -> Descripción -> Fecha -> Categoria.
        //Por eso sale solo el updateTitle. Cuando se actualize el titulose actualizarán en el orden anterior.
        serviceViewModel.updateTitle(service.id, binding.editServiceTitle.text.toString())
    }

    /**
     * Sube el servico a la base de datos
     */
    private fun uploadService() {
        val subcategory = binding.spinnerSubcategory.selectedItem
        val subcategoryName = subcategory?.toString() ?: ""
        serviceViewModel.saveService(
            Service(
                id = idServiceRandom,
                title = binding.editServiceTitle.text.toString(),
                description = binding.editDescription.text.toString(),
                category = CategoryMap(
                    binding.spinnerCategory.selectedItem.toString(),
                    subcategoryName
                ),
                image = imagesUploaded,
                userid = Constants.USER_LOGGED_IN_ID,
                edited_time = LocalDate.now().format(DateTimeFormatter.ofPattern(("dd-MM-yyyy")))

            )
        )
    }

    private var isAllSelected = false

    /**
     * Selecciona todas la imagenes
     */
    private fun selectAll() {
        if (!isAllSelected) adapter.selectAll()
        else adapter.unselectAll()

        isAllSelected = !isAllSelected
    }

    /**
     * Borra las imagenes seleccionadas
     */
    private fun removeImage() {
        adapter.removeItems()
        binding.selectAll.isChecked = false

    }

    /**
     * Actualiza el recycler view con las imagenes seleccionadas
     */
    private fun managePhotosUri(uriList: List<Uri>?) {
        var num = 0
        uriList?.forEach {
            imagesList.add(Image(num.toString(), it.toString(), num, false))
            num++
        }
        adapter.notifyDataSetChanged()
    }

    var storagePermission = false
    var cameraPermission = false

    /**
     * Comprueva si los persmidos para la camara y galeria estan dados,
     * si no pedira los permisos al usuario
     */
    private fun checkPermissions(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
        permissions.forEach { actionMap ->
            when (actionMap.key) {
                CAMERA -> {
                    cameraPermission = actionMap.value
                }
                READ_EXTERNAL_STORAGE -> {
                    storagePermission = actionMap.value
                }
            }
        }
    }

    /**
     * Añade una imagen
     */
    private fun addImage() {
        if ((ActivityCompat.checkSelfPermission(
                requireContext(),
                colum[0]
            ) != PackageManager.PERMISSION_GRANTED) &&
            (ActivityCompat.checkSelfPermission(
                requireContext(),
                colum[1]
            ) != PackageManager.PERMISSION_GRANTED)
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().requestPermissions(colum, 123)
            }
        } else {
            cameraPermission = true
            storagePermission = true
        }
        if (cameraPermission && storagePermission) {
            openGallery()
        }

    }

    /**
     * Abre la galeria del dispositivo
     */
    private fun openGallery() {
        getContent.launch("image/*")
    }


    lateinit var imagesRecyclerView: RecyclerView
    lateinit var adapter: ImagesAdapter
    var imagesList = ArrayList<Image>()

    /**
     * Inicializa el recycler view
     */
    private fun initRecyclerView() {
        imagesRecyclerView = binding.listImages
        //LinearLayoutManager HORIZONTAL
        //serviceRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL,false)
        imagesRecyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        //Asignación del adaptador al recyclerview.

        adapter = ImagesAdapter(imagesList)
        imagesRecyclerView.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewService.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
