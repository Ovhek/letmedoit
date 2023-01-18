package cat.copernic.letmedoit.domain.usecases.reports

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.domain.repositories.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de eliminar un reporte
 * @constructor Inyecta una instancia del repositorio de reportes
 * @property reportRepository el repositorio de reportes para realizar la operación.
 */

class DeleteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /**
     * Método para invocar el caso de uso de eliminar un reporte
     * @param idReport id del reporte a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(idReport: String): Flow<DataState<Boolean>> =
        reportRepository.deleteReport(idReport)
}