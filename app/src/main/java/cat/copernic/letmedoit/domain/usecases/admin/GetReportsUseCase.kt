package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import cat.copernic.letmedoit.domain.repositories.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de obtener los reportes.
 * @constructor Inyecta una instancia del repositorio de reportes
 * @property reportRepository el repositorio de reportes para realizar la operación de obtener los reportes
*/
class GetReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /**
     * Método para invocar el caso de uso de obtener los reportes.
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
    */
    suspend operator fun invoke(): Flow<DataState<List<Report>>> = reportRepository.getReports()
}