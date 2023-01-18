package cat.copernic.letmedoit.presentation.adapter.general.viewholder

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.presentation.view.general.fragments.*
import cat.copernic.letmedoit.Utils.Utils
import cat.copernic.letmedoit.data.model.Opinion
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.OpinionsUserTemplateBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
/**
 * ViewHolder de opiniones
 */

class OpinionsViewHolder(val binding: OpinionsUserTemplateBinding) : RecyclerView.ViewHolder(binding.root)  {

    val profileImage = binding.profileImage
    val serviceName = binding.serviceName
    val description = binding.description
    val rating = binding.userRating
    val nameSurname = binding.nameSurname

    val opinion = binding.opinionLayout

    @SuppressLint("SetTextI18n")
    fun render(opinionsModel: Opinion, user: Users, service: Service){
        //Asigación de datos a los controles del view.
        serviceName.text = service.title
        description.text = opinionsModel.description
        rating.rating = opinionsModel.rating
        nameSurname.text = "${user.name} ${user.surname}"
        if(user.avatar != "") Picasso.get().load(user.avatar).into(binding.profileImage)

        opinion.setOnClickListener { goToUser(user.id) }

        if(FirebaseAuth.getInstance().currentUser == null){
            binding.btnReport.isEnabled = false
            binding.btnReport.isVisible = false
        }


        binding.btnReport.setOnClickListener { Utils.goToUserReport(itemView, user.id) }

    }

    /**
     * Ir al perfil del usario
     * @param id id del usuario
     */
    private fun goToUser(id: String) {
        var action  = PerfilUsuarioMenuSuperiorDirections.actionPerfilUsuarioMenuSuperiorSelf(userID = id)

        Navigation.findNavController(itemView).navigate(action)
    }
}
