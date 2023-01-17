package cat.copernic.letmedoit.presentation.view.general.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.FragmentProfileMoreInfoBinding
import dagger.hilt.android.AndroidEntryPoint


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragment que infla la informacion del perfil
 */
@AndroidEntryPoint
class ProfileMoreInfo(private val user: Users) : Fragment() {
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

    lateinit var binding: FragmentProfileMoreInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileMoreInfoBinding.inflate(inflater, container, false)

        binding.btnPdf.setOnClickListener { user.curriculum?.let { it1 -> openPDF(it1) } }
        binding.btnEmail.setOnClickListener { user.contactInfo?.let { it1 -> sendEmail(it1.email) } }
        binding.btnMobile.setOnClickListener { user.contactInfo?.let { it1 -> callUser(it1.phone) } }
        binding.locationIcon.setOnClickListener { user.location?.let { it1 -> openMaps(it1) } }

        binding.aboutMeText.text = user.aboutMe
        binding.scheduleText.text = "${user.schedule?.initHour} -  ${user.schedule?.endHour}"
        binding.locationText.text = user.location
        return binding.root
    }

    /**
     * Abre un pdf en el navegador web
     */
    private fun openPDF(url: String) {
        if (url != "") {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            startActivity(browserIntent)
        }
    }

    /**
     * Envia un email al email de contacto del perfil
     */
    private fun sendEmail(address: String) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:${address}") // only email apps should handle this

        startActivity(
            Intent.createChooser(
                intent,
                resources.getString(R.string.sendEmail)
            )
        )
    }

    /**
     * Llama al al numero de telefono del perfil
     */
    private fun callUser(telf: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:${telf}")
        startActivity(
            Intent.createChooser(
                dialIntent,
                resources.getString(R.string.callusing)
            )
        )
    }

    /**
     * Abre la ubicacion en google maps del perfil
     */
    private fun openMaps(location: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${location}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(
            Intent.createChooser(
                mapIntent,
                resources.getString(R.string.showmapusing)
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileMoreInfo.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): ProfileMoreInfo {
            val user = Users()
            return ProfileMoreInfo(user).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        }
    }

}