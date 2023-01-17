package cat.copernic.letmedoit.presentation.viewmodel.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.domain.usecases.admin.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Una clase ViewModel que conecta la vista con el repositorio de Categories.
 * Proporciona objetos LiveData para mostrar el estado actual de las operaciones.
 * Todas las funciones de esta clase actuan sobre la base de datos.
 * Utiliza los UseCase para comunicarse con el repositorio
 * @param newCategoryUseCase instancia de InsertCategoryUseCase
 * @param insertSubcategoryUseCase instancia de InsertSubcategoryUseCase
 * @param getCategoriesUseCase instancia de GetCategoriesUseCase
 * @param deleteCategoryUseCase instancia de DeleteCategoryUseCase
 * @param updateCategoryDescUseCase instancia de UpdateCategoryDescUseCase
 * @param updateCategoryNameUseCase instancia de UpdateCategoryNameUseCase
 * @param deleteSubcategoryUseCase instancia de DeleteSubcategoryUseCase
 */
@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    val newCategoryUseCase: InsertCategoryUseCase,
    val insertSubcategoryUseCase: InsertSubcategoryUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase,
    val deleteCategoryUseCase: DeleteCategoryUseCase,
    val updateCategoryDescUseCase: UpdateCategoryDescUseCase,
    val updateCategoryNameUseCase: UpdateCategoryNameUseCase,
    val deleteSubcategoryUseCase: DeleteSubcategoryUseCase
) : ViewModel() {
    /**
     * Estado de la actualizacion del nombre de una categoria en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateNameCategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val updateNameCategoryState: LiveData<DataState<Boolean>> get() = mUpdateNameCategoryState

    /**
     * Estado de la actulizacion de la descripcion de una categoria en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mUpdateDescCategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val updateDescCategoryState: LiveData<DataState<Boolean>> get() = mUpdateDescCategoryState

    /**
     * Estado de insertar una subcategoria en la subcoleccion de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mIsertSubcategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val insertCategoryState: LiveData<DataState<Boolean>> get() = mIsertSubcategoryState

    /**
     * Estado de guardar un categoria en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mNewCategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val newCategoryState: LiveData<DataState<Boolean>> get() = mNewCategoryState

    /**
     * Estado de eliminar una subcategoria de la subcoleccion en la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mDeleteSubcategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val deleteSubcategoryState: LiveData<DataState<Boolean>> get() = mDeleteSubcategoryState

    /**
     * Estado de eliminar una categoria de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mDeleteCategoryState: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val deleteCategoryState: LiveData<DataState<Boolean>> get() = mDeleteCategoryState

    /**
     * Estado de lectura de todas las categorias de la base de datos
     * Puede contener 4 valores (DataState)
     */
    private val mGetCategoriesState: MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val getCategoriesState: LiveData<DataState<List<Category>>> get() = mGetCategoriesState

    /**
     * Funcion que actualiza el nombre de una categoria en la base de datos
     * Utiliza updateCategoryNameUseCase para invocar la funcion del repositorio
     * @param id id de la categoria
     * @param name nuevo nombre
     */
    fun updateName(id: String, name: String) {
        viewModelScope.launch {
            updateCategoryNameUseCase(id, name).onEach { dataState ->
                mUpdateNameCategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que actualiza la descripcion de una categoria en la base de datos
     * Utiliza updateCategoryDescUseCase para invocar la funcion del repositorio
     * @param id id de la categoria
     * @param desc nueva descripcion
     */
    fun updateDesc(id: String, desc: String) {
        viewModelScope.launch {
            updateCategoryDescUseCase(id, desc).onEach { dataState ->
                mUpdateDescCategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que inserta una subcategoria en la subcoleccion de subcategorias
     * de la categoria indicada.
     * Utiliza insertSubcategoryUseCase para invocar la funcion del repositorio
     * @param categoryId id de la categoria
     * @param subcategory Instancia de Subcategory
     */
    fun insertSubategory(categoryId: String, subcategory: Subcategory) {
        viewModelScope.launch {
            insertSubcategoryUseCase(categoryId, subcategory).onEach { dataState ->
                mIsertSubcategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que guarda una categoria en la base de datos
     * Utiliza newCategoryUseCase para invocar la funcion del repositorio
     * @param category Instancia de Category
     */
    fun insertCategory(category: Category) {
        viewModelScope.launch {
            newCategoryUseCase(category).onEach { dataState ->
                mNewCategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que elimina una categoria de la base de datos
     * Utiliza deleteCategoryUseCase para invocar la funcion del repositorio
     * @param id id de la categoria
     */
    fun deleteCategory(id: String) {
        viewModelScope.launch {
            deleteCategoryUseCase(id).onEach { dataState ->
                mDeleteCategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que elimina una subcategoria
     * Utiliza deleteSubcategoryUseCase para invocar la funcion del repositorio
     * @param categoryId id de la categoria
     * @param subcategoryId id de la subcategoria
     */
    fun deleteSubcategory(categoryId: String, subcategoryId: String) {
        viewModelScope.launch {
            deleteSubcategoryUseCase(categoryId, subcategoryId).onEach { dataState ->
                mDeleteSubcategoryState.value = dataState
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Funcion que lee todas las categorias de la base de datos
     * Utiliza getCategoriesUseCase para invocar la funcion del repositorio
     */
    fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().onEach { dataState ->
                mGetCategoriesState.value = dataState
            }.launchIn(viewModelScope)
        }
    }
}