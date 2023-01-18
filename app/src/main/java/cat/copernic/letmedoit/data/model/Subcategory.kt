package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Una clase de datos que representa una subcategoria.
 *
 * @param nombre el nombre de la subcategoria.
 * @param description la descripcion de la subcategoria.
 * @param id el id único de la subcategoria generado con UUID.randomUUID().toString().
 */
@Keep
@Parcelize
data class Subcategory(
    val nombre : String = "",
    val description: String = "",
    val id : String = UUID.randomUUID().toString()
):Parcelable
