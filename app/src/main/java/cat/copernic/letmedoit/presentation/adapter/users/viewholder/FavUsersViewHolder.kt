package cat.copernic.letmedoit.presentation.adapter.users.viewholder

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.presentation.view.general.fragments.*
import cat.copernic.letmedoit.databinding.ItemViewFavUserBinding
import cat.copernic.letmedoit.presentation.adapter.users.FavUsersAdapter
import cat.copernic.letmedoit.presentation.view.users.fragments.viewFavUsers
import com.squareup.picasso.Picasso

/**
 * ViewHolder de usuarios favoritos
 */
class FavUsersViewHolder(val binding: ItemViewFavUserBinding): RecyclerView.ViewHolder(binding.root) {
    val user_name = binding.txtUserName
    val favBtn = binding.userFav

    @SuppressLint("SetTextI18n")
    fun render(userModel: Users){

        user_name.text = "${userModel.name} ${userModel.surname} \n @${userModel.username}"
        if(!userModel.avatar.isEmpty()) Picasso.get().load(userModel.avatar).into(binding.imgFaUserProfile)
        favBtn.setOnClickListener { (this.bindingAdapter as FavUsersAdapter).deleteFavUser(userModel) }
        binding.favUserLayout.setOnClickListener{ userModel.id?.let { it1 -> goToUserTopMenu(it1) } }
    }

    /**
     * Dirige al perfil del usuario
     * @param userId id del usuario
     */
    private fun goToUserTopMenu(userId : String) {
        val action = profiles_services_manager_visDirections.actionProfilesServicesManagerVisToPerfilUsuarioMenuSuperior(userID = userId)
        Navigation.findNavController(itemView).navigate(action)
    }
}