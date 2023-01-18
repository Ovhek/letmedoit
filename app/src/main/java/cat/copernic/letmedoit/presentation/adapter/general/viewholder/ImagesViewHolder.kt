package cat.copernic.letmedoit.presentation.adapter.general.viewholder

import android.net.Uri
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.databinding.ImagesListTemplateBinding
import com.squareup.picasso.Picasso

/**
 * ViewHolder de imagenes
 */
class ImagesViewHolder(val binding : ImagesListTemplateBinding) : RecyclerView.ViewHolder(binding.root) {

    val checkBox = binding.itemSelected


    private lateinit var onCheckedChangeListener : CompoundButton.OnCheckedChangeListener

    val img = binding.imgService
    fun render(imageModel: Image){

        checkBox.isChecked = imageModel.checked == true

        checkBox.tag = imageModel
        Picasso.get().load(Uri.parse(imageModel.img_link)).into(img)
        onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                val item: Image = buttonView.tag as Image
                item.checked = isChecked
            }
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener)
    }

}