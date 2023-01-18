package cat.copernic.letmedoit.domain.repositories

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Report
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define los métodos para interactuar con un repositorio de reportes.
 */
interface ReportRepository {
    /**
     * Método para crear un nuevo reporte.
     * @param report instancia de la clase {@link Report} a crear
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun createReport(report: Report): Flow<DataState<Boolean>>

    /**
     * Método para eliminar un reporte.
     * @param reportId id del reporte a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteReport(reportId: String): Flow<DataState<Boolean>>

    /**
     * Método para obtener todos los reportes.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun getReports(): Flow<DataState<List<Report>>>
}