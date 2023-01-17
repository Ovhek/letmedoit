package cat.copernic.letmedoit.presentation.view.users.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.Constants
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.Utils.datahepers.UsersMap
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentUserReportBinding
import cat.copernic.letmedoit.presentation.view.general.fragments.PerfilUsuarioMenuSuperiorArgs
import cat.copernic.letmedoit.presentation.viewmodel.general.ReportsViewModel
import cat.copernic.letmedoit.presentation.viewmodel.users.UserViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que infla y gestiona la pantalla para reportar a un usario
 * Utiliza ViewModel para utilzar las funciones de los repositorios.
 */
@AndroidEntryPoint
class UserReport : Fragment() {
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

    lateinit var binding: FragmentUserReportBinding
    private val args: UserReportArgs by navArgs()

    //ViewModels que comunican  la vista con el repositorio (bd)
    private val userViewModel: UserViewModel by viewModels()
    private val reportViewModel: ReportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserReportBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getUser(args.userID)
        //Iniciar listeners
        initListeners()
        //Inicar obervers encargados de mostrar el estado de las operacions de los repositorios
        initObservers()
    }

    private lateinit var user: Users

    /**
     * Funcion que inicia los obsevers
     * Monitoriza la insercion de reportes y lectura de usuarios de la base de datos
     */
    private fun initObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Users?> -> {
                    dataState.data?.let { user = it }
                    initView()
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
                }
                else -> Unit
            }
        })
        reportViewModel.createReportState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    Utils.showOkDialog(
                        resources.getString(R.string.reportinfo),
                        requireContext(),
                        resources.getString(R.string.userreportedtmsg),
                        requireActivity()
                    )
                    requireActivity().onBackPressed()
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
                    binding.btnReport.isEnabled = false
                }
                else -> Unit
            }
        })
    }

    /**
     * Inicia los listeners
     */
    private fun initListeners() {
        binding.reportUserBackArrow.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnReport.setOnClickListener { reportUser() }
    }

    /**
     * Reporta a un usuario
     */
    private fun reportUser() {
        if (!isDataSet()) return
        reportViewModel.createReport(buildReport())
    }

    /**
     * Avisa al usuario mediante un dialogo si no ha elegido una razon para el reporte
     */
    private fun isDataSet(): Boolean {
        if (binding.reportReason.checkedRadioButtonId == -1) {
            Utils.showOkDialog(
                resources.getString(R.string.unfilledInfo),
                requireContext(),
                resources.getString(R.string.noreportreason),
                requireActivity()
            )
            return false
        }
        if (binding.editCommentText.text.toString().isBlank()) {
            Utils.showOkDialog(
                resources.getString(R.string.unfilledInfo),
                requireContext(),
                resources.getString(R.string.noreportcomment),
                requireActivity()
            )
            return false
        }
        return true
    }

    /**
     * Crea un reporte
     * @return Report
     */
    private fun buildReport(): Report {
        var reasonId = when (binding.reportReason.checkedRadioButtonId) {
            R.id.report_misbehaviour -> 1
            R.id.report_fraud -> 2
            else -> 3
        }
        return Report(
            UUID.randomUUID().toString(),
            UsersMap(Constants.USER_LOGGED_IN_ID, user.id),
            binding.editCommentText.text.toString(),
            reasonId,
            Constants.USER_LOGGED_IN.username,
            user.username,
            false
        )
    }

    /**
     * Inicia la configuracion de la vista
     */
    @SuppressLint("SetTextI18n")
    private fun initView() {
        if (user.avatar != "") Picasso.get().load(user.avatar).into(binding.userImage)

        binding.userName.text = "${user.name} ${user.surname} \n @${user.username}"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserReport.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserReport().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}