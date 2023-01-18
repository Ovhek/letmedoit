package cat.copernic.letmedoit.presentation.adapter.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Opinion
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.databinding.OpinionsUserTemplateBinding
import cat.copernic.letmedoit.presentation.adapter.general.viewholder.OpinionsViewHolder
/**
 * Adapter de opiniones
 */
class OpinionsAdapter(
    private var opinionsList: ArrayList<Opinion>,
    private val users: ArrayList<Users>,
    private val services: ArrayList<Service>
) : RecyclerView.Adapter<OpinionsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpinionsViewHolder {
        val binding = OpinionsUserTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OpinionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OpinionsViewHolder, position: Int) {
        holder.render(opinionsList[position],users[position],services[position])
    }

    override fun getItemCount(): Int {
        return opinionsList.size
    }


}
