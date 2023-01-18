package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de eliminar un servicio de la colección de usuarios
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class DeleteServiceUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Método para invocar el caso de eliminar un servicio de la colección de usuarios
     * @param idService id del service
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(idService : String) : Flow<DataState<Boolean>> = userRepository.deleteService(idService)
}