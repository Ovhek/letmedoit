package cat.copernic.letmedoit.presentation.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.databinding.ItemListSubcategoryAdminBinding
import cat.copernic.letmedoit.presentation.adapter.admin.viewholder.AdminSubcategoryViewHolder
/**
 * Adapter de subcategorias
 */
class AdminSubcategoryAdapter(
    private val subcategoryList: List<Subcategory>,
    private val onClickDelete:(Int) -> Unit,
):RecyclerView.Adapter<AdminSubcategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminSubcategoryViewHolder {
        val binding = ItemListSubcategoryAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminSubcategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminSubcategoryViewHolder, position: Int) {
        val item = subcategoryList[position]
        holder.render(item,onClickDelete)
    }

    override fun getItemCount(): Int = subcategoryList.size

}