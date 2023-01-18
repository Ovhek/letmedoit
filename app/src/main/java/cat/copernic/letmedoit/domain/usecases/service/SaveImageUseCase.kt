package cat.copernic.letmedoit.domain.usecases.service

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import cat.copernic.letmedoit.domain.repositories.ServiceRepository
import javax.inject.Inject

/**
 * Clase para el caso de uso de guardar una imagen de servicio
 * @constructor Inyecta una instancia del repositorio de servicios
 * @property serviceRepository el repositorio de servicios para realizar la operación.
 */
class SaveImageUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {
    /**
     * Método para invocar el caso de guardar una imagen de servicio
     * @param fileURI dirección de la imagen a guardar
     * @param serviceId id del servicio
     * @param index el indice de la imagen a editar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(fileURI : Uri, serviceId : String, index : Int) = serviceRepository.saveServiceImage(fileURI,serviceId,index)
}