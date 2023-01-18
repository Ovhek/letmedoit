package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de eliminar un curriculum del usuario (pdf) del storage
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class DeleteCurriculumFromStorageUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Método para invocar el caso de eliminar un curriculum del usuario (pdf) del storage
     * @param pdfLink dirección del pdf a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(pdfLink : String) : Flow<DataState<Boolean>> = userRepository.deleteCurriculumFromStorage(pdfLink)
}