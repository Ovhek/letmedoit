package cat.copernic.letmedoit.data.model

import androidx.annotation.Keep
import cat.copernic.letmedoit.Utils.datahepers.UsersMap
import com.google.firebase.firestore.Exclude

/**
 * Una clase de datos que representa una conversación.
 *
 * @param id el id único de la conversación.
 * @param users los usuarios de la conversación.
 * @param messagesUserOne los mensajes del primer usuario de la conversación.
 * @param messageUserTwo los mensajes del segundo usuario de la conversación.
 * @param lastMessage el último mensaje de la conversación.
 */
@Keep
data class Chat (
    @get:Exclude val id :   String,
    val users : UsersMap,
    val messagesUserOne :   ArrayList<Message>,
    val messageUserTwo :    ArrayList<Message>,
    val lastMessage :       String

)