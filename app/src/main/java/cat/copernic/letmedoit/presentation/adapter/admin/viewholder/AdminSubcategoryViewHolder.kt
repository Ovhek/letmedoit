package cat.copernic.letmedoit.presentation.adapter.admin.viewholder

import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.databinding.ItemListSubcategoryAdminBinding
/**
 * ViewHolder de subcategorias
 */
class AdminSubcategoryViewHolder(
    val binding: ItemListSubcategoryAdminBinding
) : RecyclerView.ViewHolder(binding.root) {

    val subcategoryName = binding.txtSubcategoryName

    fun render(
        subcategoryModel: Subcategory,
        onClickDelete: (Int) -> Unit,
    ) {

        subcategoryName.text = subcategoryModel.nombre.replace("_", "\n")
        binding.itemSubcategoryDeleteIcon.setOnClickListener {
            onClickDelete(absoluteAdapterPosition)
        }
    }
}