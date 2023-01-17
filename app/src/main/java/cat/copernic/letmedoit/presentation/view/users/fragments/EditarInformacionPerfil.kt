package cat.copernic.letmedoit.presentation.view.users.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.Utils.datahepers.ContactInfoMap
import cat.copernic.letmedoit.Utils.datahepers.ScheduleMap
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentEditarInformacionPerfilBinding
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Fragment que infla y gestiona la pantalla para editar el perfil del usuario
 * Utiliza ViewModels para comunarse con el repositorio (bd)
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class EditarInformacionPerfil : Fragment() {


    lateinit var user: Users
    val args: EditarInformacionPerfilArgs by navArgs()
    lateinit var getContent: ActivityResultLauncher<String>
    lateinit var openDocument: ActivityResultLauncher<Array<String>>
    lateinit var requestLocationPermission: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Image
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            managePhotoUri(uri)
        }
        //Document
        openDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            manageDocumentUri(uri)
        }
        //Maps
        requestLocationPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                requestPermissionsResult(it)
            }

    }

    val userViewModel: UserViewModel by viewModels()
    lateinit var binding: FragmentEditarInformacionPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditarInformacionPerfilBinding.inflate(inflater, container, false)

        initView()
        initListeners()
        initObservers()

        return binding.root
    }

    /**
     * Inicia la vista
     */
    private fun initView() {
        user = args.user

        if (user.avatar != "") Picasso.get().load(user.avatar).into(binding.profileImage)
        binding.nameSurname.text = "${user.name} ${user.surname} \n @${user.username} \n"
        binding.aboutMeText.text = user.aboutMe
        binding.scheduleText.text = "${user.schedule?.initHour} - ${user.schedule?.endHour}"
        binding.locationText.text = user.location
    }

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.backArrow.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnEditProfileImage.setOnClickListener { editProfileImage() }
        binding.btnEditNameSurname.setOnClickListener { editNameSurname() }
        binding.btnEditAboutMe.setOnClickListener { editAboutMe() }
        binding.btnEditCurriculum.setOnClickListener { editCurriculum() }
        binding.btnEditSchedule.setOnClickListener { editSchedule() }
        binding.btnEditContactInfo.setOnClickListener { editContactInfo() }
        binding.btnEditLocation.setOnClickListener { editLocation() }
        binding.btnEditPassword.setOnClickListener { editPassword() }

        binding.btnPdf.setOnClickListener { user.curriculum?.let { it1 -> openPDF(it1) } }
        binding.btnEmail.setOnClickListener { user.contactInfo?.let { it1 -> sendEmail(it1.email) } }
        binding.btnMobile.setOnClickListener { user.contactInfo?.let { it1 -> callUser(it1.phone) } }
        binding.btnLocationIcon.setOnClickListener { user.location?.let { it1 -> openMaps(it1) } }
    }


    /**
     * Inicia los obervers que monitorizan el proceso de las operacions con la base de datos
     */
    private fun initObservers() {
        userViewModel.deleteAvatarFromStorageState.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Success<Boolean> -> {

                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        hideImageProgressBar()
                    }
                    is DataState.Loading -> {
                        showImageProgressBar()
                    }
                    else -> Unit
                }
            })

        userViewModel.addAvatarToStorageState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<String> -> {
                    userViewModel.updateAvatar(dataState.data)
                    user.avatar = dataState.data
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    hideImageProgressBar()
                }
                is DataState.Loading -> {
                    showImageProgressBar()
                }
                else -> Unit
            }
        })

        userViewModel.updateAvatarState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    if (dataState.data)
                        if (user.avatar != "") Picasso.get().load(user.avatar)
                            .into(binding.profileImage)
                    hideImageProgressBar()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    hideImageProgressBar()
                }
                is DataState.Loading -> {}
                else -> Unit
            }
        })
        userViewModel.updateAboutMeState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                    binding.aboutMeText.text = user.aboutMe
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )

                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
        userViewModel.deleteCurriculumFromStorageState.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Success<Boolean> -> {
                        user.curriculum = ""
                        binding.btnEditCurriculum.isEnabled = true
                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        binding.btnEditCurriculum.isEnabled = true
                        binding.btnPdf.isVisible = true
                        binding.curriculumLoading.isVisible = false
                    }
                    is DataState.Loading -> {
                        binding.btnEditCurriculum.isEnabled = false
                    }
                    else -> Unit
                }
            })
        userViewModel.addCurriculumToStorageState.observe(
            viewLifecycleOwner,
            Observer { dataState ->
                when (dataState) {
                    is DataState.Success<String> -> {
                        userViewModel.updateCurriculum(dataState.data)
                        user.curriculum = dataState.data
                    }
                    is DataState.Error -> {
                        Utils.showOkDialog(
                            "${resources.getString(R.string.error)}",
                            requireContext(),
                            dataState.exception.message.toString(),
                            requireActivity()
                        )
                        binding.btnEditCurriculum.isEnabled = true
                        binding.btnPdf.isVisible = true
                        binding.curriculumLoading.isVisible = false
                    }
                    is DataState.Loading -> {
                        binding.btnEditCurriculum.isEnabled = false
                    }
                    else -> Unit
                }
            })
        userViewModel.updateCurriculumState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )

                }
                is DataState.Loading -> {
                    binding.btnPdf.isVisible = false
                    binding.curriculumLoading.isVisible = true
                }
                else -> {
                    binding.btnEditCurriculum.isEnabled = true
                    binding.btnPdf.isVisible = true
                    binding.curriculumLoading.isVisible = false
                }
            }
        })
        userViewModel.updateScheduleState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                    binding.scheduleText.text =
                        "${user.schedule?.initHour} - ${user.schedule?.endHour}"
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false

                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
        userViewModel.updateContactInfoState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false

                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
        userViewModel.updateLocationState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                    binding.locationText.text = user.location
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false

                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
        userViewModel.updateSurnameState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                    binding.nameSurname.text = "${user.name} ${user.surname} \n @${user.username}"
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false

                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
        userViewModel.updatePasswordState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    dialog.dismiss()
                }
                is DataState.Error -> {
                    Utils.showOkDialog(
                        "${resources.getString(R.string.error)}",
                        requireContext(),
                        dataState.exception.message.toString(),
                        requireActivity()
                    )
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = false

                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = true
                    btn.text = resources.getText(R.string.done)
                }
                is DataState.Loading -> {
                    dialogBinding.findViewById<ProgressBar>(R.id.progress).isVisible = true
                    val btn = dialogBinding.findViewById<Button>(R.id.btn_done)
                    btn.isEnabled = false
                    btn.text = ""
                }
                else -> Unit
            }
        })
    }

    /**
     * Oculta la animacion de carga al subir la imagen de perfil
     */
    private fun hideImageProgressBar() {
        binding.btnEditProfileImage.isEnabled = true
        binding.profileImage.alpha = 1.0f
        binding.loginLoading.isVisible = false
    }

    /**
     * Muestra la animacin de carga al subir la imagen de perfil
     */
    private fun showImageProgressBar() {
        binding.btnEditProfileImage.isEnabled = false
        binding.profileImage.alpha = 0.0f
        binding.loginLoading.isVisible = true
    }

    lateinit var dialogBinding: View
    lateinit var dialog: Dialog
    lateinit var googleMap: GoogleMap
    lateinit var mapView: MapView
    var markers = ArrayList<Marker>()
    lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Edita la ubicacion del usuario
     */
    @SuppressLint("MissingPermission")
    private fun editLocation() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_location, null)
        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)


        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }

        val locationText = dialogBinding.findViewById<TextView>(R.id.textLocation)
        dialogBinding.findViewById<Button>(R.id.btn_search)
            .setOnClickListener { searchLocation(locationText.text.toString()) }
        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            val textLocation = locationText.text.toString()
            userViewModel.updateLocation(textLocation)
            user.location = textLocation
        }

        locationText.text = user.location

        mapView = dialogBinding.findViewById<MapView>(R.id.mapView)

        MapsInitializer.initialize(requireActivity())
        mapView.onCreate(dialog.onSaveInstanceState())
        mapView.onResume()

        val geoCoder = Geocoder(requireContext())

        mapView.getMapAsync {
            googleMap = it
            if (checkPermission())
                it.isMyLocationEnabled = true
            else
                askPermission()

            it.setOnMyLocationButtonClickListener {

                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val address =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 1)[0]
                    locationText.text = "${address.getAddressLine(0)}"
                }
                false
            }

            it.setOnMapClickListener { location ->
                val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)[0]

                markers.forEach { it.remove() }

                val marker = googleMap.addMarker(MarkerOptions().position(location))

                if (marker != null) {
                    markers.add(marker)
                }

                locationText.text = "${address.getAddressLine(0)}"
            }
        }
        dialog.show()
    }

    /**
     * Busca la ubicacion introducida por el usuario
     * @param text Ubicacion
     */
    private fun searchLocation(text: String) {
        if (text.isEmpty()) return

        markers.forEach { it.remove() }
        val geoCoder = Geocoder(requireContext())
        val address = geoCoder.getFromLocationName(text, 1)[0]

        val latLng = LatLng(address.latitude, address.longitude)
        val marker = googleMap.addMarker(MarkerOptions().position(latLng).title(text))
        if (marker != null) {
            markers.add(marker)
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));

    }

    /**
     * Pregunta por los permisos de Ubicacion
     */
    private fun askPermission() {
        dialog.dismiss()
        requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Actualiza el permiso de Ubicacion
     * @param accepted Boolean
     */
    @SuppressLint("MissingPermission")
    private fun requestPermissionsResult(accepted: Boolean) {
        if (accepted) {
            googleMap.isMyLocationEnabled = true
            dialog.show()
        }
    }

    /**
     * Comprueba los permimos
     * @return true si el permiso esta dado/ false si no esta dado
     */
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Edita la informacion de conta del perfil de usuario
     */
    private fun editContactInfo() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_contact_info, null)

        dialogBinding.findViewById<TextInputEditText>(R.id.textEmail).setText(
            user.contactInfo?.email
                ?: ""
        )
        dialogBinding.findViewById<TextInputEditText>(R.id.textPhoneNumber).setText(
            user.contactInfo?.phone
                ?: ""
        )

        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            val textEmail =
                dialogBinding.findViewById<TextInputEditText>(R.id.textEmail).text.toString()
            val textPhone =
                dialogBinding.findViewById<TextInputEditText>(R.id.textPhoneNumber).text.toString()

            val phoneRegex =
                "^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?".toRegex()
            val validPhone = textPhone.matches(phoneRegex)
            val validEmail = Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()

            if (!validPhone && !validEmail) Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.invalidPhone) + "\n" + resources.getString(R.string.notvalidemail),
                requireActivity()
            )
            else if (!validPhone) Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.invalidPhone),
                requireActivity()
            )
            else if (!validEmail) Utils.showOkDialog(
                resources.getString(R.string.error),
                requireContext(),
                resources.getString(R.string.notvalidemail),
                requireActivity()
            )
            else {
                val contactInfo = ContactInfoMap(textEmail, textPhone)
                userViewModel.updateContactInfo(contactInfo)
                user.contactInfo = contactInfo
            }
        }
        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
    }

    /**
     * Edita el horario del perfil de usario
     */
    private fun editSchedule() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_schedule, null)

        dialogBinding.findViewById<TextInputEditText>(R.id.textInitHour).setText(
            user.schedule?.initHour
                ?: ""
        )
        dialogBinding.findViewById<TextInputEditText>(R.id.textEndHour).setText(
            user.schedule?.endHour
                ?: ""
        )

        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.show()


        val textInitHour = dialogBinding.findViewById<TextInputEditText>(R.id.textInitHour)
        val textEndHour = dialogBinding.findViewById<TextInputEditText>(R.id.textEndHour)
        textInitHour.setOnClickListener {
            timePicker(textInitHour, null)
        }
        textEndHour.setOnClickListener {
            timePicker(null, textEndHour)
        }
        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            if (initTime != "" && endTime != "") {
                val schedule = ScheduleMap(initTime, endTime)
                userViewModel.updateSchedule(schedule)
                user.schedule = schedule
            } else {
                Utils.showOkDialog(
                    resources.getString(R.string.txt_num_info),
                    requireContext(),
                    resources.getString(R.string.initAndEndTime),
                    requireActivity()
                )
            }
        }
        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
    }

    /**
     * Abre un dialogo para seleccionar el horario
     */
    private fun timePicker(textInitHour: TextInputEditText?, textEndHour: TextInputEditText?) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(
            requireContext(),
            timeIsSet(textInitHour, textEndHour),
            hour,
            minute,
            true
        ).show()
    }

    private var initTime = ""
    private var endTime = ""
    private fun timeIsSet(textInitHour: TextInputEditText?, textEndHour: TextInputEditText?) =
        TimePickerDialog.OnTimeSetListener() { _, hour, minute ->
            val timeString = String.format("%02d:%02d", hour, minute)
            if (textInitHour != null) {
                textInitHour.setText(timeString)
                initTime = textInitHour.text.toString()
            }
            if (textEndHour != null) {
                textEndHour.setText(timeString)
                endTime = textEndHour.text.toString()
            }
        }

    /**
     * Edita la contraseña de usuario
     */
    private fun editPassword() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_edit_password, null)

        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            val textEmail =
                dialogBinding.findViewById<TextInputEditText>(R.id.textEmail).text.toString().trim()
            val textOldPassword =
                dialogBinding.findViewById<TextInputEditText>(R.id.editOldPassword).text.toString()
            val textNewPassword =
                dialogBinding.findViewById<TextInputEditText>(R.id.editNewPassword).text.toString()

            userViewModel.updatePassword(textOldPassword, textNewPassword, textEmail)

        }
        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
    }

    /**
     * Gestiona la subida e iliminacion del curriculum de la base de datos
     * @param uri
     */
    private fun manageDocumentUri(uri: Uri?) {
        if (uri == null)
            return

        if (user.curriculum != "")
            userViewModel.deleteCurriculumFromStorage(user.curriculum)

        userViewModel.addCurriculumToStorage(uri)
    }

    /**
     * Edita el curriculum del perfil
     */
    private fun editCurriculum() {
        openDocument.launch(arrayOf("application/pdf"))
    }

    /**
     * Edita la informacion del perfil
     */
    private fun editAboutMe() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_about_me, null)
        dialogBinding.findViewById<TextInputEditText>(R.id.textAboutMe).setText(user.aboutMe)

        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            val aboutMeText =
                dialogBinding.findViewById<TextInputEditText>(R.id.textAboutMe).text.toString()
            userViewModel.updateAboutMe(aboutMeText)
            user.aboutMe = aboutMeText
        }
        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
    }

    /**
     * Edita el nombre y apellido del usuario
     */
    private fun editNameSurname() {
        dialogBinding = layoutInflater.inflate(R.layout.dialog_name_surname, null)

        dialogBinding.findViewById<TextInputEditText>(R.id.textName).setText(
            user.name
                ?: ""
        )
        dialogBinding.findViewById<TextInputEditText>(R.id.textSurname).setText(
            user.surname
                ?: ""
        )

        dialog = Dialog(binding.root.context)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.findViewById<Button>(R.id.btn_done).setOnClickListener {
            val textName =
                dialogBinding.findViewById<TextInputEditText>(R.id.textName).text.toString()
            val textSurname =
                dialogBinding.findViewById<TextInputEditText>(R.id.textSurname).text.toString()


            userViewModel.updateName(textName)
            userViewModel.updateSurname(textSurname)

            user.name = textName
            user.surname = textSurname

        }
        dialogBinding.findViewById<Button>(R.id.btn_cancel).setOnClickListener { dialog.dismiss() }
    }

    /**
     * Gestiona la subida e elimnacion de la imagen de perfil
     * @param uri
     */
    private fun managePhotoUri(uri: Uri?) {
        if (uri == null)
            return

        if (user.avatar != "")
            userViewModel.deleteAvatarFromStorage(user.avatar!!)

        userViewModel.addAvatarToStorage(uri)
    }

    /**
     * Edita la imagen de perfil
     */
    private fun editProfileImage() {
        getContent.launch("image/*")
    }

    /**
     * Abre un PDF en el navegador web
     * @param url
     */
    private fun openPDF(url: String) {

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)


        startActivity(browserIntent)
    }

    /**
     * Envia un email
     * @param address
     */
    private fun sendEmail(address: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:${address}") // only email apps should handle this

        startActivity(
            Intent.createChooser(
                intent,
                "Send Email Using: "
            )
        )
    }

    /**
     * Llama al usuario al numero de telefono del perfil
     * @param telf
     */
    private fun callUser(telf: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:${telf}")
        startActivity(
            Intent.createChooser(
                dialIntent,
                "Call Using: "
            )
        )
    }

    /**
     * Abre el google maps con la ubicacion del perfil
     * @param location
     */
    private fun openMaps(location: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${location}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(
            Intent.createChooser(
                mapIntent,
                "Show Map Using: "
            )
        )
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!checkPermission()) {
            if (::dialog.isInitialized) {
                dialog.dismiss()
                googleMap.isMyLocationEnabled = false
                dialog.show()
                askPermission()
            }

        }
    }
}