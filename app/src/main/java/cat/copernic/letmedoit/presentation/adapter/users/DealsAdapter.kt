package cat.copernic.letmedoit.presentation.adapter.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.Utils.datahepers.DealsUsersServicesJoin
import cat.copernic.letmedoit.databinding.ItemVerDealsBinding
import cat.copernic.letmedoit.presentation.adapter.users.viewholder.DealsViewHolder
/**
 * Adapter de tratos del usuario
 */
class DealsAdapter(
    private val dealsToShow: ArrayList<DealsUsersServicesJoin>,
) : RecyclerView.Adapter<DealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsViewHolder {

        val binding =
            ItemVerDealsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealsViewHolder, position: Int) {

        val item = dealsToShow[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = dealsToShow.size
}