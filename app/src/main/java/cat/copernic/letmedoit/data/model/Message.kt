package cat.copernic.letmedoit.data.model

import androidx.annotation.Keep
/**
 * Una clase de datos que representa un mensaje.
 *
 * @param id el id único del mensaje.
 * @param text el texto del mensaje.
 * @param created la fecha de creación del mensaje.
 */
@Keep
data class Message (
    val id : String,
    val text : String,
    val created : String
)