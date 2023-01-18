package cat.copernic.letmedoit.domain.usecases.user

import android.net.Uri
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase que representa el caso de uso para añadir un currículum a almacenamiento.
 * @param userRepository Repositorio de usuarios desde donde se obtendrá la lógica necesaria para añadir un currículum a almacenamiento.
*/
class AddCurriculumToStorageUseCase @Inject constructor(
private val userRepository: UserRepository
){
    /**
     * Método que añade un currículum a almacenamiento.
     * @param uri La ruta del currículum que se desea añadir.
     * @return Un flujo con el estado del resultado de la operación.
    */
    suspend operator fun invoke(uri: Uri) : Flow<DataState<String>> = userRepository.addCurriculumToStorage(uri)
}

