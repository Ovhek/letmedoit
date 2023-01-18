package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import cat.copernic.letmedoit.Utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de editar una imagen del servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */
class GetAllServicesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    suspend operator fun invoke() : Flow<DataState<List<Service>>> = serviceRepository.getAllServices()
}