package cat.copernic.letmedoit.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import cat.copernic.letmedoit.Utils.datahepers.*
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList
/**
 * Una clase de datos que representa un usuario.
 *
 * @param id el id único del usuario generado con UUID.randomUUID().toString().
 * @param name el nombre del usuario.
 * @param surname el apellido del usuario.
 * @param email el correo electrónico del usuario.
 * @param language el idioma seleccionado por el usuario.
 * @param darkTheme el tema oscuro seleccionado por el usuario.
 * @param avatar el avatar del usuario.
 * @param curriculum el curriculum del usuario.
 * @param schedule el horario del usuario.
 * @param aboutMe información adicional del usuario.
 * @param contactInfo la información de contacto del usuario.
 * @param location la ubicación del usuario.
 * @param servicesId los id de los servicios del usuario.
 * @param favorites los favoritos del usuario.
 * @param chatsId los id de las conversaciones del usuario.
 * @param historyDeals el historial de tratos del usuario.
 * @param opinions las opiniones del usuario.
 * @param check el estado de revisión del usuario.
 * @param rating la calificación promedio del usuario.
 * @param banned el estado de prohibición del usuario.
 * @param admin si el usuario es administrador o no.
 * @param username el nombre de usuario del usuario.
 * @param deviceToken el token del dispositivo del usuario.
 */
@Keep
@Parcelize
data class Users(
    @get:Exclude var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var surname: String = "",
    val email: String = "",
    val language: Int = 0,
    val darkTheme: Boolean = false,
    var avatar: String = "",
    var curriculum: String = "",
    var schedule: ScheduleMap = ScheduleMap("",""),
    var aboutMe: String = "",
    var contactInfo: ContactInfoMap = ContactInfoMap("",""),
    var location: String = "",
    @get:Exclude var servicesId: ArrayList<UserServices>? = null,
    @get:Exclude val favorites: FavoritesMap? = null,
    @get:Exclude val chatsId: ArrayList<UserChats>? = null,
    @get:Exclude val historyDeals: ArrayList<HistoryDeal>? = null,
    @get:Exclude var opinions: ArrayList<Opinion> = ArrayList(),
    @get:Exclude var check: Boolean = false,
    var rating: Float = 0f,
    var banned: Boolean = false,
    val admin: Boolean = false,
    val username: String = "",
    val deviceToken : String = ""
    ) : Parcelable