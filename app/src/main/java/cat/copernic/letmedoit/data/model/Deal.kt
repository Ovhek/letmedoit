package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import cat.copernic.letmedoit.Utils.datahepers.ServicesMap
import cat.copernic.letmedoit.Utils.datahepers.UsersMap
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Una clase de datos que representa un trato.
 *
 * @param id el id único del trato.
 * @param users los usuarios del trato.
 * @param services los servicios del trato.
 * @param description la descripción del trato.
 * @param accepted el estado del trato.
 * @param conclude la conclusión del trato.
 */
@Keep
@Parcelize
data class Deal(
    @get:Exclude var id :   String = UUID.randomUUID().toString(),
    val users : UsersMap = UsersMap("",""),
    val services : ServicesMap = ServicesMap("",""),
    val description:        String = "",
    var accepted :          Boolean = false,
    var conclude:           Int = 0
) :Parcelable