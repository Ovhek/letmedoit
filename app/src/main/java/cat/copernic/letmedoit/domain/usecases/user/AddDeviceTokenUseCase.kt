package cat.copernic.letmedoit.domain.usecases.user

import android.net.Uri
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase que representa el caso de uso para añadir un token de dispositivo.
 * @param userRepository Repositorio de usuarios desde donde se obtendrá la lógica necesaria para añadir un token de dispositivo.
*/
class AddDeviceTokenUseCase @Inject constructor(
private val userRepository: UserRepository
){
    /**
     * Función que agrega el token de dispositivo a un usuario
     * @param token Token de dispositivo a ser agregado
     * @return Flow<DataState<Boolean>> representando el estado de la operación
    */
    suspend operator fun invoke(token: String) : Flow<DataState<Boolean>> = userRepository.addDeviceToken(token)
}