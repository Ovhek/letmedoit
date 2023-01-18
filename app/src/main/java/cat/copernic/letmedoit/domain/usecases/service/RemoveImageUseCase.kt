package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de eliminar una imagen de un servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */

class RemoveImageUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    /**
     * Método para invocar el caso de eliminar una imagen de un servicio
     * @param idService id del servicio
     * @param imgLink la dirección dela imagen
     * @param imgIndex el indice de la imagen a editar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(idService : String, imgIndex : Int,imgLink : String) : Flow<DataState<Boolean>> = serviceRepository.removeImage(idService,imgIndex,imgLink)
}