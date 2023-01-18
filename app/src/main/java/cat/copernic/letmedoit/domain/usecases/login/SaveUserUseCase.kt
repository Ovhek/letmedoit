package cat.copernic.letmedoit.domain.usecases.login

import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de guardar el usuario
 * @constructor Inyecta una instancia del repositorio de login
 * @property loginRepository el repositorio de login para realizar la operación.
 */

class SaveUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    /**
     * Método para invocar el caso de uso de guardar el usuario
     * @param user el usuario a guardar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(user : Users) : Flow<DataState<Boolean>> = loginRepository.saveUser(user)
}