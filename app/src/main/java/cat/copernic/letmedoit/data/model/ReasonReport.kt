package cat.copernic.letmedoit.data.model

import com.google.firebase.firestore.Exclude

/**
 * Una clase de datos que representa una razón de reporte.
 *
 * @param id el id único de la razón de reporte.
 * @param name el nombre de la razón de reporte.
 */
data class ReasonReport(
    @get:Exclude val id : Int,
    val name: String
)