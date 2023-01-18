package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Una clase de datos que representa una opinión.
 *
 * @param id el id único de la opinión generado con UUID.randomUUID().toString().
 * @param userId el id del usuario que hace la opinión.
 * @param serviceId el id del servicio del cual se esta haciendo la opinión.
 * @param rating la calificación del servicio.
 * @param description la descripción de la opinión.
 * @param deal_id el id del trato relacionado con la opinión.
 */
@Keep
@Parcelize
data class Opinion (

    @get:Exclude val id : String = UUID.randomUUID().toString(),
    val userId :          String = "",
    val serviceId :       String = "",
    val rating  :       Float = 0f,
    val description:    String = "",
    val deal_id :       String = ""

) : Parcelable