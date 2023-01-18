package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.data.model.Subcategory
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de insertar una subcategoria.
 * @constructor Inyecta una instancia del repositorio de categorías
 * @property categoryRepository el repositorio de categorías para realizar la operaciónde insertar una subcategoria
*/
class InsertSubcategoryUseCase @Inject constructor(
    val categoryRepository: CategoryRepository
) {
    /**
    * Método para invocar el caso de uso de insertar una subcategoria.
    * @param categoryId el id de la categoria a la que se insertara la subcategoria
    * @param subcategory la subcategoria a insertar
    * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
    */
    suspend operator fun invoke(categoryId: String,subcategory: Subcategory): Flow<DataState<Boolean>> = categoryRepository.insertSubcategory(categoryId,subcategory)
}
