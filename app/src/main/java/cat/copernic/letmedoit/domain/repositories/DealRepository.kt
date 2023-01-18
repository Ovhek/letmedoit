package cat.copernic.letmedoit.domain.repositories

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Deal
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define los métodos para interactuar con un repositorio de negociaciones.
 */
interface DealRepository {
    /**
     * Método para insertar una nueva negociación.
     * @param deal instancia de la clase {@link Deal} a insertar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun insertDeal(deal: Deal): Flow<DataState<Boolean>>

    /**
     * Método para rechazar una negociación.
     * @param id id de la negociación a rechazar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun denyDeal(id: String): Flow<DataState<Boolean>>

    /**
     * Método para aceptar una negociación.
     * @param id id de la negociación a aceptar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun acceptDeal(id: String): Flow<DataState<Boolean>>

    /**
     * Método para concluir una negociación.
     * @param id id de la negociación a concluir
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun concludeDeal(id: String): Flow<DataState<Boolean>>

    /**
     * Método para obtener una negociación específica.
     * @param id id de la negociación a obtener
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getDeal(id : String) : Flow<DataState<Deal>>

    /**
     * Método para obtener todas las negociaciones.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getDeals() : Flow<DataState<List<Deal>>>

    /**
     * Método para suscribirse a las actualizaciones de una negociación específica.
     * @param idDeal id de la negociación a la cual se desea suscribir
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun suscribeForUpdates(idDeal : String) : Flow<DataState<Deal?>>
}