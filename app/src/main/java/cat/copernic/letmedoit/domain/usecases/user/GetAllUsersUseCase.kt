package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.data.model.Users
import cat.copernic.letmedoit.domain.repositories.ReportRepository
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de obtener todos los usuarios de la base de datos
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    /**
     * Método para invocar el caso de obtener todos los usuarios de la base de datos
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(): Flow<DataState<List<Users>>> = userRepository.getAllUsers()
}