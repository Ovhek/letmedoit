package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de actualizar la categoría del servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */

class UpdateCategoryUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    /**
     * Método para invocar el caso de actualizar la categoría del servicio
     * @param idService id del servicio
     * @param newCategoryMap nueva categoría
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */

    suspend operator fun invoke(idService : String, newCategoryMap : CategoryMap) : Flow<DataState<Boolean>> = serviceRepository.updateCategory(idService,newCategoryMap)
}