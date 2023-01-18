package cat.copernic.letmedoit.domain.usecases.deals

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.domain.repositories.DealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de concluir un trato
 * @constructor Inyecta una instancia del repositorio de tratos
 * @property dealRepository el repositorio de tratos para realizar la operación.
 */
class ConcludeDealUseCase @Inject constructor(
    private val dealRepository: DealRepository
){
    /**
     * Método para invocar el caso de uso concluir un trato
     * @param id el id del trato a concluir
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(id: String): Flow<DataState<Boolean>> = dealRepository.concludeDeal(id)
}