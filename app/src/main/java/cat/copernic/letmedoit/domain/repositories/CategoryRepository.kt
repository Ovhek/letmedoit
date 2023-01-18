package cat.copernic.letmedoit.domain.repositories

import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Subcategory
import kotlinx.coroutines.flow.Flow

/**
* Interfaz que define los métodos para interactuar con un repositorio de categorías.
*/
interface CategoryRepository {

    /**
    * Método para insertar una nueva categoría.
    * @param category instancia de la clase {@link Category} a insertar
    * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
    */
    suspend fun insertCategory(category: Category): Flow<DataState<Boolean>>
    /**
    * Método para insertar una nueva subcategoría.
    * @param categoryId id de la categoria a la cual pertenece la subcategoria
    * @param subcategory instancia de la clase {@link Subcategory} a insertar
    * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend fun insertSubcategory(categoryId:String,subcategory: Subcategory): Flow<DataState<Boolean>>
    /**
    * Método para obtener todas las categorías.
    * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend fun getCategories(): Flow<DataState<List<Category>>>
    /**
     * Método para eliminar una categoría.
     * @param id id de la categoria a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteCategory(id: String): Flow<DataState<Boolean>>
    /**
     * Método para eliminar una subcategoria.
     * @param categoryId id de la categoria a la cual pertenece la subcategoria
     * @param subcategoryId id de la subcategoria a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun deleteSubcategory(categoryId:String, subcategoryId: String): Flow<DataState<Boolean>>
    /**
     * Método para actualizar el nombre de una categoria.
     * @param idCategory id de la categoria a actualizar
     * @param newNombre nuevo nombre para la categoria
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     */
    suspend fun updateNombre(idCategory: String, newNombre: String): Flow<DataState<Boolean>>
    /**
     * Método para actualizar la descripcion de una categoria.
     * @param idCategory id de la categoria a actualizar
     * @param newDescription nueva descripcion para la categoria
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend fun updateDescription(idCategory: String, newDescription: String): Flow<DataState<Boolean>>
}