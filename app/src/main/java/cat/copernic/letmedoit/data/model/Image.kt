package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

/**
 * Una clase de datos que representa una imagen.
 *
 * @param id el id único de la imagen.
 * @param img_link el link de la imagen.
 * @param index el indice de la imagen.
 * @param checked el estado de la imagen.
 */
@Keep
@Parcelize
data class Image (
    @get:Exclude var id : String = "",
    val img_link : String = "",
    val index : Int = 0,
    @get:Exclude var checked : Boolean? = false
) : Parcelable