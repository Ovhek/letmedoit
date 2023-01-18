package cat.copernic.letmedoit.domain.usecases.service

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de actualizar el número de likes de un servicio.
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */
class UpdateNLikesUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
){
    /**
     * Método para invocar el caso de actualizar el número de likes de un servicio.
     * @param idService id del servicio
     * @param newNum nuevo valor de los likes
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */

    suspend operator fun invoke(idService : String, newNum : Int) : Flow<DataState<Boolean>> = serviceRepository.updateNLikes(idService,newNum)
}