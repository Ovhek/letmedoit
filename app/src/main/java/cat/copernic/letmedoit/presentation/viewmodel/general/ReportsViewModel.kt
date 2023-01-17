package cat.copernic.letmedoit.presentation.viewmodel.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Chat
import cat.copernic.letmedoit.data.model.Report
import cat.copernic.letmedoit.domain.usecases.admin.GetReportsUseCase
import cat.copernic.letmedoit.domain.usecases.reports.CreateReportUseCase
import cat.copernic.letmedoit.domain.usecases.reports.DeleteReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Una clase ViewModel que conecta la vista con el repositorio de Reports.
 * Proporciona objetos LiveData para mostrar el estado actual de las operaciones.
 * Todas las funciones de esta clase actuan sobre la base de datos.
 * Utiliza los UseCase para comunicarse con el repositorio
 * @param createReportUseCase instancia de CreateReportUseCase
 * @param deleteReportUseCase instancia de DeleteReportUseCase
 * @param getReportsUseCase instancia de GetReportsUseCase
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val createReportUseCase: CreateReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase,
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {

    /**
     * Estado de lectura de todos los reportes de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mGetReportState: MutableLiveData<DataState<List<Report>>> = MutableLiveData()
    val getReportState: LiveData<DataState<List<Report>>>
        get() = mGetReportState

    /**
     * Estado de crear un reporte en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mCreateReportState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val createReportState: LiveData<DataState<Boolean>>
        get() = mCreateReportState

    /**
     * Estado de eliminar un reporte de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mDeleteReportState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val deleteReportState: LiveData<DataState<Boolean>>
        get() = mDeleteReportState

    /**
     * Funcion que lee todos los reportes de la base de datos
     * Utiliza getReportsUseCase para invocar la funcion del repositorio
     */
    fun getReports() {
        viewModelScope.launch {
            getReportsUseCase().onEach { dataState ->
                mGetReportState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que guarda un reporte en la base de datos
     * Utiliza createReportUseCase para invocar la funcion del repositorio
     * @param report Instancia de la clase Report
     */
    fun createReport(report: Report) {
        viewModelScope.launch() {
            createReportUseCase(report)
                .onEach { dataState ->
                    mCreateReportState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que elimina un reporte de la base de datos
     * Utiliza deleteReportUseCase para invocar la funcion del repositorio
     * @param idReport id del reporte
     */
    fun deleteReport(idReport: String) {
        viewModelScope.launch() {
            deleteReportUseCase(idReport)
                .onEach { dataState ->
                    mDeleteReportState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}