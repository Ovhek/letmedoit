package cat.copernic.letmedoit.presentation.viewmodel.general

import android.app.Activity
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.letmedoit.data.model.Service
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.Utils.datahepers.CategoryMap
import cat.copernic.letmedoit.domain.usecases.service.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Una clase ViewModel que conecta la vista con el repositorio de Services.
 * Proporciona objetos LiveData para mostrar el estado actual de las operaciones.
 * Todas las funciones de esta clase actuan sobre la base de datos.
 * Utiliza los UseCase para comunicarse con el repositorio
 *@param getAllServicesUseCase  objeto de GetAllServicesUseCase
 *@param getServiceUseCase  objeto de GetServiceUseCase
 *@param saveServiceUseCase  objeto de SaveServiceUseCase
 *@param saveImageUseCase  objeto de SaveImageUseCase
 *@param updateTitleUseCase  objeto de UpdateTitleUseCase
 *@param updateEditedTimeUseCase  objeto de UpdateEditedTimeUseCase
 *@param updateNLikesUseCase  objeto de UpdateNLikesUseCase
 *@param updateDescriptionUseCase  objeto de UpdateDescriptionUseCase
 *@param updateCategoryUseCase  objeto de UpdateCategoryUseCase
 *@param removeImageUseCase  objeto de RemoveImageUseCase
 *@param editServiceImageUseCase  objeto de EditServiceImageUseCase
 *@param removeServiceUseCase  objeto de RemoveServiceUseCase
 */
@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val getAllServicesUseCase: GetAllServicesUseCase,
    private val getServiceUseCase: GetServiceUseCase,
    private val saveServiceUseCase: SaveServiceUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val updateTitleUseCase: UpdateTitleUseCase,
    private val updateEditedTimeUseCase: UpdateEditedTimeUseCase,
    private val updateNLikesUseCase: UpdateNLikesUseCase,
    private val updateDescriptionUseCase: UpdateDescriptionUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val removeImageUseCase: RemoveImageUseCase,
    private val editServiceImageUseCase: EditServiceImageUseCase,
    private val removeServiceUseCase: RemoveServiceUseCase
) : ViewModel() {

    /**
     * Estado de lectura de un servicio de la vase de datos
     * Puede contener 4 valores (DataState)
     */
    private val mGetServiceState: MutableLiveData<DataState<Service>> = MutableLiveData()

    val getServiceState: LiveData<DataState<Service>>
        get() = mGetServiceState

    /**
     * Estado de lectura de todos los servicios de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mGetServicesState: MutableLiveData<DataState<List<Service>>> = MutableLiveData()

    val getServicesState: LiveData<DataState<List<Service>>>
        get() = mGetServicesState

    /**
     *Estado de guardar un servico en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mSaveServiceState: MutableLiveData<DataState<Service>> = MutableLiveData()

    val saveServiceState: LiveData<DataState<Service>>
        get() = mSaveServiceState

    /**
     *Estado de guardar la imagen del servicio en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mSaveImageState: MutableLiveData<DataState<String>> = MutableLiveData()

    val saveImageState: LiveData<DataState<String>>
        get() = mSaveImageState

    /**
     * Funcion que lee todos los servicios de la base de datos
     * Utiliza getAllServicesUseCase para invocar la funcio del repositorio
     */
    fun getAllServices() {
        viewModelScope.launch {
            getAllServicesUseCase()
                .onEach { dataState ->
                    mGetServicesState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Lee un servicio determinado de la base de datos
     * Utilza el getServiceUseCase para invocar la funcion del repositorio
     * @param uid Id del servicio
     */
    fun getService(uid: String) {
        viewModelScope.launch {
            getServiceUseCase(uid)
                .onEach { dataState ->
                    mGetServiceState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que guarda un servicio den la base de datos
     * Utiliza saveServiceUseCase para invocar la funcion de repositorio
     * @param service instancia de la clase Servicio
     */
    fun saveService(service: Service) {
        viewModelScope.launch {
            saveServiceUseCase(service)
                .onEach { dataState ->
                    mSaveServiceState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion una imagen en la base de datos
     * Utiliza saveImageUseCase para invocar la funcion del repositorio
     * @param fileUri Uri de la imagen
     * @param serviceId id del servicio
     * @param index Indice de la imagen
     */
    fun saveImage(fileUri: Uri, serviceId: String, index: Int) {
        viewModelScope.launch {
            saveImageUseCase(fileUri, serviceId, index)
                .onEach { dataState ->
                    mSaveImageState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Estado de la actualizacion del titulo del servico en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateTitleState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val updateTitleState: LiveData<DataState<Boolean>>
        get() = mUpdateTitleState

    /**
     * Funcion que actualiza el titulo de un servicio en la base de datos
     * Utiliza updateTitleUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newTitle nuevo titulo
     */
    fun updateTitle(idService: String, newTitle: String) {
        viewModelScope.launch {
            updateTitleUseCase(idService, newTitle)
                .onEach { dataState ->
                    mUpdateTitleState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Estado de la actualizacion de la descripcion en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateDescriptionState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val updateDescriptionState: LiveData<DataState<Boolean>>
        get() = mUpdateDescriptionState

    /**
     * Funcion que actualiza la descripcion de un servicio en la base de datos
     * Utiliza updateDescriptionUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newDescription nueva descripcion
     */
    fun updateDescription(idService: String, newDescription: String) {
        viewModelScope.launch {
            updateDescriptionUseCase(idService, newDescription)
                .onEach { dataState ->
                    mUpdateDescriptionState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *  Estado de actualizacion de una imagen en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateServiceImageState: MutableLiveData<DataState<String>> = MutableLiveData()

    val updateServiceImageState: LiveData<DataState<String>>
        get() = mUpdateServiceImageState

    /**
     * Funcion que actualiza una imagen de un servicio en la base de datos
     * Utiliza editServiceImageUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newFileURI nueva imagen
     * @param index indice de la imagen en la coleccion de imagenes del servicio
     */
    fun editServiceImage(idService: String, newFileURI: Uri, index: Int) {
        viewModelScope.launch {
            editServiceImageUseCase(idService, newFileURI, index)
                .onEach { dataState ->
                    mUpdateServiceImageState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Estadp de la actulizacion de la categoria en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateCategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val updateCategoryState: LiveData<DataState<Boolean>>
        get() = mUpdateCategoryState

    /**
     * Funcion que actualiza la categoria de un servicio en la base de datos
     * Utiliza updateCategoryUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newCategoryMap Instancia de la clase CategoryMap que contiene la nueva categoria
     * y subcategoria
     */
    fun updateCategory(idService: String, newCategoryMap: CategoryMap) {
        viewModelScope.launch {
            updateCategoryUseCase(idService, newCategoryMap)
                .onEach { dataState ->
                    mUpdateCategoryState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Actulizacion de los likes en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateNLikesState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val updateNLikesState: LiveData<DataState<Boolean>>
        get() = mUpdateNLikesState

    /**
     * Funcion que actualiza los likes de un servicio en la base de datos
     * Utiliza updateNLikesUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newNum nuevo numero de likes
     */
    fun updateNLikes(idService: String, newNum: Int) {
        viewModelScope.launch {
            updateNLikesUseCase(idService, newNum)
                .onEach { dataState ->
                    mUpdateNLikesState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Actualizacion del tiempo de edicion del servicio en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateEditedTimeState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val updateEditedTimeState: LiveData<DataState<Boolean>>
        get() = mUpdateEditedTimeState

    /**
     * Funcion que actualiza el tiempo de edicion de un servicio en la base de datos
     * Utiliza updateEditedTimeUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param newEditedTime nueva fecha de edicion
     */
    fun updateEditedTime(idService: String, newEditedTime: String) {
        viewModelScope.launch {
            updateEditedTimeUseCase(idService, newEditedTime)
                .onEach { dataState ->
                    mUpdateEditedTimeState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     *Estato de la elimnación de una imagen del servico en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mRemoveImageUseCaseState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val removeImageUseCaseState: LiveData<DataState<Boolean>>
        get() = mRemoveImageUseCaseState

    /**
     * Funcion que elimina un imagen de un servicio en la base de datos
     * Utiliza removeImageUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     * @param index indice de la imagen en la coleccion de imagenes del servicio
     * @param imgLink link de la imagen
     */
    fun removeImage(idService: String, index: Int, imgLink: String) {
        viewModelScope.launch {
            removeImageUseCase(idService, index, imgLink)
                .onEach { dataState ->
                    mRemoveImageUseCaseState.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    /**
     * Estado de la eliminacion de un servicio de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mRemoveServiceState: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val removeServiceState: LiveData<DataState<Boolean>>
        get() = mRemoveServiceState

    /**
     * Funcion que elimina un  servicio de la base de datos
     * Utiliza removeServiceUseCase para invocar la funcion del repositorio
     * @param idService id del servicio
     */
    fun removeService(idService: String) {
        viewModelScope.launch {
            removeServiceUseCase(idService)
                .onEach { dataState ->
                    mRemoveServiceState.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}