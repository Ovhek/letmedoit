package cat.copernic.letmedoit.presentation.adapter.users

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.ItemViewFavUserBinding
import cat.copernic.letmedoit.presentation.adapter.users.viewholder.FavUsersViewHolder
import cat.copernic.letmedoit.presentation.view.users.fragments.viewFavUsers
import dagger.hilt.android.internal.managers.ViewComponentManager

/**
 * Adapter de usuarios favoritos
 */
class FavUsersAdapter(private val obtenerUsers: ArrayList<Users>, private val fragment: Fragment): RecyclerView.Adapter<FavUsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUsersViewHolder {
        val binding = ItemViewFavUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavUsersViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavUsersViewHolder, position: Int) {
        val item = obtenerUsers[position]
        holder.render(item)
    }
    override fun getItemCount(): Int = obtenerUsers.size

    /**
     * Borrar un usuario faborito del recycler view
     * @param user Users
     */
    fun deleteFavUser(user: Users){
        val position = obtenerUsers.indexOf(user)
        obtenerUsers.remove(user)
        notifyItemRemoved(position)

        user.id?.let { (fragment as viewFavUsers).deleteFavoriteProfile(it) }
    }

    /**
     * Limpiar el recycler view
     */
    fun clear(){
        val size = obtenerUsers.size
        obtenerUsers.clear()
        notifyItemRangeRemoved(0,size)
    }
}