package cat.copernic.letmedoit.presentation.adapter.admin.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.databinding.ItemListCategoryAdminBinding
/**
 * ViewHolder de categorias
 */
class AdminCategoryViewHolder(val binding: ItemListCategoryAdminBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val categoryIcon = binding.imgCategory
    val categoryName = binding.txtCategoryName
    val subcategoryNumber = binding.txtSubcategoryNumber
    fun render(
        categoryModel: Category,
        onClickListener: (Category, Int) -> Unit,
        onClickDelete: (Int) -> Unit,
        onClickEdit: (Category) -> Unit
    ) {
        categoryIcon.background = ContextCompat.getDrawable(
            binding.root.context,
            binding.root.resources.getIdentifier(
                categoryModel.image,
                "drawable",
                "cat.copernic.letmedoit"
            )
        )
        categoryName.text = categoryModel.nombre.replace("_","\n")
        subcategoryNumber.text = categoryModel.subcategories.size.toString()
        itemView.setOnClickListener { onClickListener(categoryModel,absoluteAdapterPosition) }
        binding.itemCategoryDeleteIcon.setOnClickListener { onClickDelete(absoluteAdapterPosition)}
        binding.itemCategoryEditIcon.setOnClickListener {onClickEdit(categoryModel) }
    }
}