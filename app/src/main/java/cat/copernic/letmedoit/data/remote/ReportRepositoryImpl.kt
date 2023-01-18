package cat.copernic.letmedoit.data.remote

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Chat
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.di.FirebaseModule
import cat.copernic.letmedoit.domain.repositories.ReportRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Clase que implementa la interfaz ReportRepository que permite conectarse a la base de datos remota de Firebase y realizar operaciones de CRUD con los reportes.
 * Utiliza la librería de coroutines de Kotlin para manejar operaciones asíncronas y emitir flujos de datos (flow) para informar el estado de las operaciones.
 *
 * @param reportsCollection referencia a la colección de reportes en la base de datos de Firebase. Inyectado mediante la anotación.
 *
 */
class ReportRepositoryImpl @Inject constructor(
    @FirebaseModule.ReportsCollection val reportsCollection: CollectionReference
) : ReportRepository {


    /**
     * Obtiene el listado de reportes de la base de datos.
     */
    override suspend fun getReports(): Flow<DataState<List<Report>>> =
        flow<DataState<List<Report>>> {
            emit(DataState.Loading)
            try {
                val reports = reportsCollection.get().await().toObjects(Report::class.java)

                emit(DataState.Success(reports))
                emit(DataState.Finished)
            } catch (e: Exception) {
                emit(DataState.Error(e))
                emit(DataState.Finished)
            }
        }.flowOn(Dispatchers.IO)

    /**
     * Crea un reporte en la base de datos.
     * @param report reporte a añadir en la base de datos.
     */
    override suspend fun createReport(report: Report): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadStatus: Boolean = false
            report.id.let {
                reportsCollection.document(it).set(report, SetOptions.merge())
                    .addOnSuccessListener { uploadStatus = true }
                    .addOnFailureListener { uploadStatus = false }
                    .await()
            }
            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Elimina un reporte de la base de datos.
     * @param reportId id del reporte a eliminar,
     */
    override suspend fun deleteReport(reportId: String): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)
        try {
            var uploadStatus: Boolean = false

            reportsCollection.document(reportId).delete()
                .addOnSuccessListener { uploadStatus = true }
                .addOnFailureListener { uploadStatus = false }
                .await()

            emit(DataState.Success(uploadStatus))
            emit(DataState.Finished)
        } catch (e: Exception) {
            emit(DataState.Error(e))
            emit(DataState.Finished)
        }
    }.flowOn(Dispatchers.IO)
}