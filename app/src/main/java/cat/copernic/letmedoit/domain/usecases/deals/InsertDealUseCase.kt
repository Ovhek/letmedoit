package cat.copernic.letmedoit.domain.usecases.deals

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Deal
import cat.copernic.letmedoit.domain.repositories.DealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de insertar un trato en la base de datos
 * @constructor Inyecta una instancia del repositorio de tratos
 * @property dealRepository el repositorio de tratos para realizar la operación.
 */

class InsertDealUseCase @Inject constructor(
    private val dealRepository: DealRepository
){
    /**
     * Clase para el caso de uso de insertar un trato
     * @constructor Inyecta una instancia del repositorio de tratos
     * @property dealRepository el repositorio de tratos para realizar la operación.
     */

    suspend operator fun invoke(deal: Deal) : Flow<DataState<Boolean>> = dealRepository.insertDeal(deal)
}

