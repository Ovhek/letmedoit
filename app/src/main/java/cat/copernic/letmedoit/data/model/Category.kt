package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

/**
* Una clase de datos que representa una categoría.
* @param nombre nombre de la categoría.
* @param description descripción de la categoría.
* @param subcategorías las subcategorías de la categoría.
* @param image la url de la imagen de la categoría.
* @param id el id único de la categoría generado con UUID.randomUUID().toString()
 * */
@Keep
@Parcelize
data class Category(
    var nombre: String = "",
    var description: String = "",
    @get:Exclude var subcategories: ArrayList<Subcategory> = arrayListOf(Subcategory("","","")),
    val image: String = "",
    val id : String = UUID.randomUUID().toString(),
) : Parcelable
