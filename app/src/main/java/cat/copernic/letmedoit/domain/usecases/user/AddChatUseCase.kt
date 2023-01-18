package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase que representa el caso de uso para añadir un chat.
 * @param userRepository Repositorio de usuarios desde donde se obtendrá la lógica necesaria para añadir un chat.
*/
class AddChatUseCase @Inject constructor(
private val userRepository: UserRepository
){
    /**
     * Método que añade un chat.
     * @param idChat El id del chat que se desea añadir.
     * @return Un flujo con el estado del resultado de la operación.
    */
    suspend operator fun invoke(idChat: String) : Flow<DataState<Boolean>> = userRepository.addChat(idChat)
}
