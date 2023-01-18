package cat.copernic.letmedoit.domain.usecases.deals

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.domain.repositories.DealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de suscribirse a los cambios de un trato
 * @constructor Inyecta una instancia del repositorio de tratos
 * @property dealRepository el repositorio de tratos para realizar la operación.
 */

class SuscribeForUpdatesUseCase @Inject constructor(
    private val dealRepository: DealRepository
){
    /**
     * Método para invocar el caso de uso de suscribirse a los cambios de un trato
     * @param id el id del trato al cual suscribirse
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(idDeal: String) : Flow<DataState<Deal?>> = dealRepository.suscribeForUpdates(idDeal)
}