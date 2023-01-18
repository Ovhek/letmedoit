package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import cat.copernic.letmedoit.Utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de guardar un servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */
class SaveServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    /**
     * Método para invocar el caso de guardar un servicio
     * @param service el servicio a guardar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(service : Service) : Flow<DataState<Service>> = serviceRepository.saveService(service)
}