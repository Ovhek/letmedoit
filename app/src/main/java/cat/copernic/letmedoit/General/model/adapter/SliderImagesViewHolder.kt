package cat.copernic.letmedoit.General.model.adapter

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.letmedoit.General.model.Image
import cat.copernic.letmedoit.R
import cat.copernic.letmedoit.Utils.ZoomableImage
import com.squareup.picasso.Picasso


class SliderImagesViewHolder(val view : View) : RecyclerView.ViewHolder(view)  {

    val imageView: ImageView = view.findViewById(R.id.slider_image)

    private var isImageFitToScreen = false

    fun setImage(curImg: Image) {
        Picasso.get().load(Uri.parse(curImg.img_link)).into(imageView)
    }
}