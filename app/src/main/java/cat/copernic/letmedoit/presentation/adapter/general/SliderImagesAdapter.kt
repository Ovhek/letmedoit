package cat.copernic.letmedoit.presentation.adapter.general

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.presentation.adapter.general.viewholder.SliderImagesViewHolder
/**
 * Adapter de imagenes
 */
class SliderImagesAdapter(private val images : ArrayList<Image>) : RecyclerView.Adapter<SliderImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderImagesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_image_slider,parent,false)
        return SliderImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderImagesViewHolder, position: Int) {
        val curImg = images[position]
        holder.setImage(curImg)
    }

    override fun getItemCount(): Int {
        return images.size
    }

}