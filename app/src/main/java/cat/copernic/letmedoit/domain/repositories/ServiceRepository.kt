package cat.copernic.letmedoit.domain.repositories

import android.net.Uri
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import kotlinx.coroutines.flow.Flow


/**
 * Interfaz que define los métodos para interactuar con un repositorio de servicios.
 */
interface ServiceRepository {

    //add
    /**
     * Método para guardar un servicio
     * @param service el servicio a guardar en la base de datos.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun saveService(service : Service) : Flow<DataState<Service>>
    /**
     * Método para guardar una imagen de servicio
     * @param fileUri Dirección de la imagen
     * @param serviceId Id del servicio del cual guardar la imagen
     * @param index Indice de la imagen al guardarla
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun saveServiceImage(fileURI : Uri, serviceId : String,index : Int): Flow<DataState<String>>

    //get
    /**
     * Método para obtener un servicio especifico
     * @param uid ID del servicio a obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getService(uid : String) : Flow<DataState<Service>>

    /**
     * Método para obtener todos los servicios de la base de datos
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getAllServices() : Flow<DataState<List<Service>>>

    //delete
    /**
     * Método para eliminar una imagen de un servicio
     * @param idService el id del servicio dond está la imagen
     * @param imgIndex el indice de la imagen en el servicio
     * @param imgLink la dirección de la imagen
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun removeImage(idService: String, imgIndex: Int,imgLink : String) : Flow<DataState<Boolean>>
    /**
     * Método para eliminar un servicio
     * @param idService id del servicio a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun removeService(idService : String) : Flow<DataState<Boolean>>

    //update
    /**
     * Método para actualizar el titulo del servicio
     * @param idService id del servicio en el cual actualizar
     * @param newTitle nuevo valor del titulo.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateTitle(idService : String, newTitle : String): Flow<DataState<Boolean>>
    /**
     * Método para actualizar la descripción  del servicio
     * @param idService id del servicio en el cual actualizar
     * @param newDescription nuevo valor de la descripción.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateDescription(idService : String, newDescription : String): Flow<DataState<Boolean>>
    /**
     * Método para actualizar la categoría  del servicio
     * @param idService id del servicio en el cual actualizar
     * @param newCategoryMap nuevo valor de la categoría
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateCategory(idService : String, newCategoryMap: CategoryMap): Flow<DataState<Boolean>>
    /**
     * Método para actualizar el número de favoritos del servicio
     * @param idService id del servicio en el cual actualizar
     * @param newNum nuevo valor del número de favoritos.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateNLikes(idService : String, newNum : Int): Flow<DataState<Boolean>>
    /**
     * Método para actualizar la última fecha de modificación
     * @param idService id del servicio en el cual actualizar
     * @param newEditedTime nuevo valor de la última vez que se actualizó el servicio.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateEditedTime(idService : String, newEditedTime : String): Flow<DataState<Boolean>>
    /**
     * Método para actualizar una imagen del servicio
     * @param idService id del servicio en el cual actualizar
     * @param newFileURI dirección de la nueva imagen
     * @param index indice de la imagen a modificar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun editServiceImage(idService : String,newFileURI : Uri, index: Int): Flow<DataState<String>>


}