package cat.copernic.letmedoit.data.model

import cat.copernic.letmedoit.Utils.datahepers.UsersMap
import com.google.firebase.firestore.Exclude
import java.util.*

/**
 * Una clase de datos que representa un reporte.
 *
 * @param id el id único del reporte generado con UUID.randomUUID().toString().
 * @param users los usuarios involucrados en el reporte.
 * @param description la descripción del reporte.
 * @param reason_id el id de la razón del reporte.
 * @param user_1 el primer usuario involucrado en el reporte.
 * @param user_2 el segundo usuario involucrado en el reporte.
 * @param archived el estado de archivado del reporte.
 * @param check el estado de revisión del reporte.
 */
data class Report(
    val id: String = UUID.randomUUID().toString(),
    val users: UsersMap = UsersMap(),
    val description: String = "",
    val reason_id: Int = 0,
    val user_1: String = "",
    val user_2: String = "",
    val archived: Boolean = false,
    @get:Exclude var check: Boolean = false
)
