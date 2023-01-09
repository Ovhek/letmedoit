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
 * A simple [Fragment] subclass.
 * Use the [UserReport.newInstance] factory method to
 * create an instance of this fragment.
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

    lateinit var binding : FragmentUserReportBinding
    private val args: UserReportArgs by navArgs()
    private val userViewModel : UserViewModel by viewModels()
    private val reportViewModel : ReportsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserReportBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getUser(args.userID)
        initListeners()
        initObservers()
    }

    private lateinit var user : Users
    private fun initObservers() {
        userViewModel.getUserState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Users?> -> {
                    dataState.data?.let { user = it }
                    initView()
                }
                is DataState.Error -> {
                    Utils.showOkDialog("Error: ",requireContext(),dataState.exception.message.toString())
                }
                is DataState.Loading -> {
                }
                else -> Unit
            }
        } )
        reportViewModel.createReportState.observe(viewLifecycleOwner, Observer { dataState ->
            when(dataState){
                is DataState.Success<Boolean> -> {
                    Utils.showOkDialog("User reported",requireContext(),"User has been reported.")
                }
                is DataState.Error -> {
                    Utils.showOkDialog("Error: ",requireContext(),dataState.exception.message.toString())
                }
                is DataState.Loading -> { binding.btnReport.isEnabled = false }
                else -> Unit
            }
        } )
    }

    private fun initListeners() {
        binding.reportUserBackArrow.setOnClickListener{ requireActivity().onBackPressed() }
        binding.btnReport.setOnClickListener{ reportUser() }
    }

    private fun reportUser() {
        if(!isDataSet()) return
        reportViewModel.createReport(buildReport())
    }

    private fun isDataSet(): Boolean {
        if(binding.reportReason.checkedRadioButtonId == -1){
            Utils.showOkDialog("Unfilled information",requireContext(),"Report reason must be indicated")
            return false
        }
        if(binding.editCommentText.text.toString().isBlank()){
            Utils.showOkDialog("Unfilled information",requireContext(),"You must make a comment for the report")
            return false
        }
        return true
    }

    private fun buildReport(): Report {
        var reasonId = when(binding.reportReason.checkedRadioButtonId){
            R.id.report_misbehaviour -> 1
            R.id.report_fraud -> 2
            else -> 3
        }
        return Report(UUID.randomUUID().toString(), UsersMap(Constants.USER_LOGGED_IN_ID,user.id),binding.editCommentText.text.toString(),reasonId,false)
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        if(user.avatar != "") Picasso.get().load(user.avatar).into(binding.userImage)

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