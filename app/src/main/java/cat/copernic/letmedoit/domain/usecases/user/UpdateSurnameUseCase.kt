package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de actualizar el apellido del usuario
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class UpdateSurnameUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Método para invocar el caso de actualizar apellido del usuario
     * @param  newSurname nuevo valor del apellido del usuario
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(newSurname: String) : Flow<DataState<Boolean>> = userRepository.updateSurname(newSurname)
}