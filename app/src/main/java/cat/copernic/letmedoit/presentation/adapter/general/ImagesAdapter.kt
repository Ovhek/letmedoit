package cat.copernic.letmedoit.presentation.adapter.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.databinding.ImagesListTemplateBinding
import cat.copernic.letmedoit.presentation.adapter.general.viewholder.ImagesViewHolder
/**
 * Adapter de imagenes
 */
class ImagesAdapter (private var imagesList : ArrayList<Image>) : RecyclerView.Adapter<ImagesViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = ImagesListTemplateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val item = imagesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    fun removeAll(){
        imagesList.removeAll(imagesList)
    }

    fun removeItems(){
        val imagesToRemove = ArrayList<Image>()

        imagesToRemove.addAll(imagesList.filter { it.checked == true })

        imagesList.removeAll(imagesToRemove)


        notifyDataSetChanged()
    }

    fun selectAll() {
        imagesList.forEach { it.checked = true }
        notifyDataSetChanged()
    }
    fun unselectAll(){
        imagesList.forEach { it.checked = false }
        notifyDataSetChanged()
    }

    fun getItems() : ArrayList<Image>{
        return imagesList
    }
}