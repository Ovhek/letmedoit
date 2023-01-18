package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de actualizar el tema oscuro del usuario
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class UpdateDarkThemeUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Método para invocar el caso de actualizar el tema oscuro del usuario
     * @param darkTheme booleano que indica si el nuevo valor del tema oscuro
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(darkTheme : Boolean) : Flow<DataState<Boolean>> = userRepository.updateDarkTheme(darkTheme)
}