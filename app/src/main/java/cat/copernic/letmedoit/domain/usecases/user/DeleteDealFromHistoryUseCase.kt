package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de eliminar un trato del historial de tratos
 * @constructor Inyecta una instancia del repositorio de usuarios
 * @property userRepository el repositorio de usuarios para realizar la operación.
 */

class DeleteDealFromHistoryUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Método para invocar el caso de eliminar un trato del historial de tratos
     * @param idDeal id del trato
     * @param idUser id del usuario 1
     * @param idUserTwo id del usuario 2
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(idDeal : String,idUser: String, idUserTwo : String) : Flow<DataState<Boolean>> = userRepository.deleteDealFromHistory(idDeal,idUser,idUserTwo)
}