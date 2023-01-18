package cat.copernic.letmedoit.domain.usecases.admin

import cat.copernic.letmedoit.Utils.DataState
import cat.copernic.letmedoit.data.model.Category
import cat.copernic.letmedoit.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Clase para el caso de uso de eliminar una categoria.
 * @constructor Inyecta una instancia del repositorio de categorías
 * @property categoryRepository el repositorio de categorías para realizar la operación de eliminar una categoria
*/
class DeleteCategoryUseCase @Inject constructor(
    val categoryRepository: CategoryRepository
) {
    /**
     * Método para invocar el caso de uso de eliminar una categoria.
     * @param id el id de la categoria a eliminar
     * @return un flujo de datos que indica el estado de la operación (success, error, loading) y el resultado de la misma
     * */
    suspend operator fun invoke(id: String): Flow<DataState<Boolean>> =
        categoryRepository.deleteCategory(id)
}