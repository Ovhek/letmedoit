package cat.copernic.letmedoit.domain.usecases.reports

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.domain.repositories.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de crear un reporte
 * @constructor Inyecta una instancia del repositorio de reportes
 * @property reportRepository el repositorio de reportes para realizar la operación.
 */
class CreateReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
){
    /**
     * Método para invocar el caso de uso de crear un reporte
     * @param report Reporte a crear y guardar en la base de datos.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(report: Report): Flow<DataState<Boolean>> = reportRepository.createReport(report)
}