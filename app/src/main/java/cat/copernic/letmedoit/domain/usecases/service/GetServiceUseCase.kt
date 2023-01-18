package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import cat.copernic.letmedoit.Utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de obtener un servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */
class GetServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    /**
     * Método para invocar el caso de obtener un servicio
     * @param uid id del servicio a obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(uid : String) : Flow<DataState<Service>> = serviceRepository.getService(uid)
}