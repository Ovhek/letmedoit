package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.Utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de insertar una categoria.
 * @constructor Inyecta una instancia del repositorio de categorías
 * @property categoryRepository el repositorio de categorías para realizar la operación de insertar una categoria
*/
class InsertCategoryUseCase @Inject constructor(
     val categoryRepository: CategoryRepository
) {
    /**
     * Método para invocar el caso de uso de insertar una categoria.
     * @param category la categoria a insertar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
    */
    suspend operator fun invoke(category: Category): Flow<DataState<Boolean>> = categoryRepository.insertCategory(category)
}