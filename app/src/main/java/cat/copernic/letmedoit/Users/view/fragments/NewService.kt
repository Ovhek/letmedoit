package cat.copernic.letmedoit.Users.view.fragments

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Selection.selectAll
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.General.model.adapter.ImagesAdapter
import cat.copernic.letmedoit.General.model.data.Image
import cat.copernic.letmedoit.General.model.provider.CategoryProvider
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.databinding.FragmentNewServiceBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewService.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList -> managePhotosUri(uriList) }
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions -> checkPermissions(permissions) }
    }
    var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        READ_EXTERNAL_STORAGE
    )


    lateinit var getContent : ActivityResultLauncher<String>
    lateinit var binding : FragmentNewServiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewServiceBinding.inflate(inflater,container,false)
        binding.btnAddImage.setOnClickListener { addImage() }
        binding.btnRemoveImage.setOnClickListener { removeImage() }
        binding.selectAll.setOnClickListener { selectAll() }
        val categoryNames = CategoryProvider.obtenerCategorias().map { it.nombre } as ArrayList<String>
        Utils.AsignarPopUpSpinner(requireContext(),categoryNames,binding.spinnerCategory)

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = CategoryProvider.obtenerCategorias()[position]
                val subCategoryNames = category.subcategories.map { it.nombre } as ArrayList<String>
                Utils.AsignarPopUpSpinner(requireContext(), subCategoryNames,binding.spinnerSubcategory)
            }

        }
        initRecyclerView()
        return binding.root
    }

    private fun selectAll() {
        adapter.selectAll()
    }

    private fun removeImage() {
        adapter.removeItems()
        binding.selectAll.isChecked = false

    }

    private fun managePhotosUri(uriList: List<Uri>?) {
        var num = 0
        uriList?.forEach {
            imagesList.add(Image(num.toString(),it.toString(),false))
            num++
        }
        adapter.notifyDataSetChanged()
    }

    var storagePermission = false
    var cameraPermission = false
    private fun checkPermissions(permissions: Map<String, @JvmSuppressWildcards Boolean>) {
        permissions.forEach{
            actionMap -> when(actionMap.key){
                CAMERA -> { cameraPermission = actionMap.value }
                READ_EXTERNAL_STORAGE ->  { storagePermission = actionMap.value }
            }
        }
    }

    private fun addImage() {
        if((ActivityCompat.checkSelfPermission( requireContext(),colum[0])!= PackageManager.PERMISSION_GRANTED) &&
            (ActivityCompat.checkSelfPermission( requireContext(),colum[1])!= PackageManager.PERMISSION_GRANTED)){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().requestPermissions(colum,123)
            }
        }
        else{
            cameraPermission = true
            storagePermission = true
        }
        if(cameraPermission && storagePermission){
            openGallery()
        }

    }

    private fun openGallery() {
        getContent.launch("image/*")
    }


    lateinit var imagesRecyclerView : RecyclerView
    lateinit var adapter : ImagesAdapter
    var imagesList = ArrayList<Image>()
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