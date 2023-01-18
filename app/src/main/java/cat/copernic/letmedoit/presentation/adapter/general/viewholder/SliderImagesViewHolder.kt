package cat.copernic.letmedoit.presentation.adapter.general.viewholder

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.data.model.Image
import cat.copernic.letmedoit.R
import com.squareup.picasso.Picasso
/**
 * ViewHolder de imagenes
 */

class SliderImagesViewHolder(val view : View) : RecyclerView.ViewHolder(view)  {

    val imageView: ImageView = view.findViewById(R.id.slider_image)

    private var isImageFitToScreen = false

    fun setImage(curImg: Image) {
        Picasso.get().load(Uri.parse(curImg.img_link)).into(imageView)
    }
}