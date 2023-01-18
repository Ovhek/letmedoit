package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de actualizar el estado de ban del usuario
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class UpdateBanUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Método para invocar el cado de actualizar estado de ban del usuario
     * @param userId id del usuario
     * @param ban estado del ban
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(userId: String, ban: Boolean): Flow<DataState<Boolean>> =
        userRepository.updateBan(userId, ban)
}