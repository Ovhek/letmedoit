package cat.copernic.letmedoit.domain.usecases.user

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase encargada de agregar un historial de tratos a un usuario
 * @property userRepository Repositorio de usuarios para hacer las operaciones necesarias
 */
class AddHistoryDealUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    /**
     * Función que agrega un historial de tratos a un usuario
     * @param idUserOne Id del primer usuario del trato
     * @param idUserTwo Id del segundo usuario del trato
     * @param idDeal Id del trato
     * @return Flow<DataState<Boolean>> representando el estado de la operación
     */
    suspend operator fun invoke(idUserOne: String,idUserTwo : String, idDeal : String) : Flow<DataState<Boolean>> = userRepository.addHistoryDeal(idUserOne,idUserTwo,idDeal)
}