package cat.copernic.letmedoit.domain.repositories

import android.net.Uri
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.datahepers.*
import cat.copernic.letmedoit.data.model.*
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz que define los métodos para interactuar con un repositorio de usuarios.
 */
interface UserRepository {
    //get
    /**
     * Método para obtener un usuario específico.
     * @param idUser id del usuario a obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getUser (idUser : String) : Flow<DataState<Users?>>

    /**
     * Método para obtener todos los usuarios.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getAllUsers():Flow<DataState<List<Users>>>

    /**
     * Método para obtener los servicios de un usuario específico.
     * @param idUser id del usuario cuyos servicios se desean obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading)
     * */
    suspend fun getServices (idUser : String) : Flow<DataState<ArrayList<UserServices>>>
    /**
     * Método para obtener los perfiles favoritos de un usuario.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getFavoriteProfiles() : Flow<DataState<ArrayList<UserFavoriteProfiles>>>
    /**
     * Método para obtener los servicios favoritos de un usuario.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getFavoriteServices() : Flow<DataState<ArrayList<UserFavoriteServices>>>

    /**
     * Método para obtener los chats de un usuario.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getChats () : Flow<DataState<ArrayList<UserChats>>>
    /**
     * Método para obtener los negociaciones (historial) de un usuario.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend fun getHistoryDeals  () : Flow<DataState<ArrayList<HistoryDeal>>>
    /**
     * Método para obtener las opiniones de un usuario específico.
     * @param idUser id del usuario cuyas opiniones se desean obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getOpinions (idUser: String) : Flow<DataState<ArrayList<Opinion>>>

//delete
    /**
     * Método para eliminar un servicio de un usuario.
     * @param idService id del servicio a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteService (idService : String) : Flow<DataState<Boolean>>

    /**
     * Método para eliminar un perfil favorito de un usuario.
     * @param idProfile id del perfil a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteFavoriteProfile(idProfile: String) : Flow<DataState<Boolean>>

    /**
     * Método para eliminar un servicio favorito de un usuario.
     * @param idService id del servicio a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteFavoriteService(idService: String) : Flow<DataState<Boolean>>

    /**
     * Método para eliminar una imagen de avatar de almacenamiento.
     * @param imgLink link de la imagen a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteAvatarFromStorage(imgLink: String) : Flow<DataState<Boolean>>

    /**
     * Método para eliminar un pdf de currículum de almacenamiento.
     * @param pdfLink link del pdf a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteCurriculumFromStorage(pdfLink : String) : Flow<DataState<Boolean>>

    /**
     * Método para eliminar una negociación del historial de un usuario.
     * @param idDeal id de la negociación a eliminar
     * @param idUserOne id del primer usuario involucrado en la negociación
     * @param idUserTwo id del segundo usuario involucrado en la negociación
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteDealFromHistory(idDeal: String, idUserOne: String, idUserTwo : String) : Flow<DataState<Boolean>>

    //add
    /**
     * Método para agregar un servicio a un usuario.
     * @param idService id del servicio a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addService(idService : String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar un perfil favorito a un usuario.
     * @param idProfile id del perfil a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addFavoriteProfiles(idProfile : String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar un servicio favorito a un usuario.
     * @param idService id del servicio a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addFavoriteServices(idService: String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar un chat a un usuario.
     * @param idChat id del chat a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addChat(idChat: String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar una negociación al historial de un usuario.
     * @param idUserOne id del primer usuario involucrado en la negociación
     * @param idUserTwo id del segundo usuario involucrado en la negociación
     * @param idDeal id de la negociación a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addHistoryDeal(idUserOne: String, idUserTwo : String, idDeal : String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar una opinión a un usuario
     * @param opinion Objeto de tipo opinion para añadir al usuario
     * @param idUser id del usuario al cual añadir la opinión
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addOpinion(opinion : Opinion,idUser : String) : Flow<DataState<Boolean>>

    /**
     * Método para agregar una imagen de avatar al storage.
     * @param fileUri Uri de la imagen a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addAvatarToStorage (fileUri: Uri): Flow<DataState<String>>

    /**
     * Método para agregar un pdf de currículum a almacenamiento.
     * @param fileUri Uri del pdf a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addCurriculumToStorage(fileUri : Uri) : Flow<DataState<String>>
    /**
     * Método para agregar un token de dispositivo a un usuario.
     * @param token token de dispositivo a agregar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun addDeviceToken(token : String) : Flow<DataState<Boolean>>

    //update
    /**
     * Método para actualizar el nombre de un usuario.
     * @param newName nuevo nombre a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateName(newName : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el apellido de un usuario.
     * @param newSurname nuevo apellido a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateSurname(newSurname : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar la contraseña de un usuario.
     * @param oldPassword contraseña actual del usuario
     * @param newPassword nueva contraseña a actualizar
     * @param email correo electrónico del usuario
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updatePassword(oldPassword : String, newPassword : String, email : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el idioma de un usuario.
     * @param language nuevo idioma a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateLanguage(language : Int) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el tema oscuro de un usuario.
     * @param darkTheme nuevo tema oscuro a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend fun updateDarkTheme(darkTheme : Boolean) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el estado de ban de un usuario.
     * @param userId id del usuario al que se le actualizará el estado de ban.
     * @param ban nuevo estado de ban a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateBan(userId:String, ban: Boolean): Flow<DataState<Boolean>>
    /**
     * Método para actualizar la imagen de avatar de un usuario.
     * @param imgLink link de la nueva imagen de avatar a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateAvatar(imgLink : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el pdf de currículum de un usuario.
     * @param pdfLink link del nuevo pdf de currículum a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateCurriculum(pdfLink : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar la información sobre mi de un usuario.
     * @param aboutMe nueva información sobre mi a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateAboutMe(aboutMe : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar el horario de un usuario.
     * @param schedule nuevo horario a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateSchedule(schedule: ScheduleMap) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar la información de contacto de un usuario.
     * @param contactInfo nueva información de contacto a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateContactInfo(contactInfo : ContactInfoMap) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar la ubicación de un usuario.
     * @param newLocation nuevaubicación a actualizar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateLocation(newLocation : String) : Flow<DataState<Boolean>>
    /**
     * Método para actualizar la calificación de un usuario.
     * @param updatedRating nueva calificación a actualizar
     * @param idUser id del usuario al que se actualizará la calificación
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateRating(updatedRating : Float,idUser: String) : Flow<DataState<Boolean>>
}