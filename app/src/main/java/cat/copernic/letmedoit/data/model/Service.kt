package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import cat.copernic.letmedoit.Utils.Constants
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Una clase de datos que representa un servicio.
 *
 * @param id el id único del servicio generado con UUID.randomUUID().toString().
 * @param title el título del servicio.
 * @param description la descripción del servicio.
 * @param category la categoria del servicio.
 * @param image las imágenes relacionadas con el servicio.
 * @param n_likes el número de likes para el servicio.
 * @param edited_time la fecha en la que el servicio fue editado por última vez.
 * @param userid el id del usuario que creo el servicio.
 * @param defaultFav el estado predeterminado del servicio en los favoritos del usuario.
 * */
@Keep
@Parcelize
data class Service(
    @get:Exclude var id : String = UUID.randomUUID().toString(),
    val title : String = "",
    val description : String = "",
    val category : CategoryMap = CategoryMap("",""),
    @get:Exclude val image: ArrayList<Image> = arrayListOf(Image("","")),
    var n_likes : Int = 0,
    val edited_time : String = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH).format(Calendar.getInstance().time),
    val userid : String = Constants.USER_LOGGED_IN_ID,
    @get:Exclude var defaultFav: Boolean = false
) : Parcelable
